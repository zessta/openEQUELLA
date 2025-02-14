/*
 * Licensed to The Apereo Foundation under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * The Apereo Foundation licenses this file to you under the Apache License,
 * Version 2.0, (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tle.core.institution.migration.v52;

import com.google.common.collect.Lists;
import com.tle.core.guice.Bind;
import com.tle.core.hibernate.impl.HibernateMigrationHelper;
import com.tle.core.migration.AbstractHibernateSchemaMigration;
import com.tle.core.migration.MigrationInfo;
import com.tle.core.migration.MigrationResult;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.inject.Singleton;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.jdbc.Work;
import org.hibernate.mapping.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This migration only applies to SQL Server. Our custom types were hardcoded to varchar, but there
 * should be no varchar columns, only nvarchar.
 *
 * @author Aaron
 */
@SuppressWarnings("nls")
@Bind
@Singleton
public class ConvertVarcharDatabaseMigration extends AbstractHibernateSchemaMigration {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(ConvertVarcharDatabaseMigration.class);

  @Override
  public boolean isBackwardsCompatible() {
    return false;
  }

  @Override
  public MigrationInfo createMigrationInfo() {
    return new MigrationInfo("com.tle.core.entity.services.migration.v52.convertvarchar");
  }

  @Override
  protected void executeDataMigration(
      final HibernateMigrationHelper helper, final MigrationResult result, Session session) {
    final Dialect dialect =
        ((SessionFactoryImplementor) helper.getFactory()).getJdbcServices().getDialect();

    if (dialect instanceof SQLServerDialect) {
      session.doWork(
          new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
              final DatabaseMetaData metaData = connection.getMetaData();
              final String defaultCatalog = helper.getDefaultCatalog();
              final String defaultSchema = helper.getDefaultSchema();

              final ResultSet tableSet =
                  metaData.getTables(defaultCatalog, defaultSchema, null, new String[] {"TABLE"});
              final List<String> tableNames = Lists.newArrayList();
              try {
                while (tableSet.next()) {
                  tableNames.add(tableSet.getString("TABLE_NAME"));
                }
              } finally {
                tableSet.close();
              }

              for (String tableName : tableNames) {
                final List<ColMeta> ntextCols = Lists.newArrayList();
                final ResultSet columnSet =
                    metaData.getColumns(defaultCatalog, defaultSchema, tableName, null);

                try {
                  while (columnSet.next()) {
                    final String colName = columnSet.getString("COLUMN_NAME");
                    final String colDbType = columnSet.getString("TYPE_NAME");
                    if (colDbType.equalsIgnoreCase("varchar")
                        || colDbType.equalsIgnoreCase("text")) {
                      LOGGER.info("varchar column found: " + tableName + "." + colName);

                      final int nullable = columnSet.getInt("NULLABLE");
                      final ColMeta colMeta = new ColMeta();
                      colMeta.size = columnSet.getInt("COLUMN_SIZE");
                      colMeta.nullable = (nullable != DatabaseMetaData.columnNoNulls);
                      colMeta.name = colName;
                      ntextCols.add(colMeta);
                    }
                  }
                } finally {
                  columnSet.close();
                }

                for (ColMeta ntextCol : ntextCols) {
                  String sizeStr =
                      ntextCol.size == Integer.MAX_VALUE ? "max" : Integer.toString(ntextCol.size);
                  final String sql =
                      "ALTER TABLE "
                          + Table.qualify(defaultCatalog, defaultSchema, tableName)
                          + " ALTER COLUMN "
                          + ntextCol.name
                          + " nvarchar("
                          + sizeStr
                          + ")"
                          + (ntextCol.nullable ? "" : " NOT")
                          + " NULL";
                  final PreparedStatement s = connection.prepareStatement(sql);
                  s.execute();
                }

                result.incrementStatus();
              }
            }
          });
    }
  }

  @Override
  protected int countDataMigrations(final HibernateMigrationHelper helper, Session session) {
    final int[] ctr = new int[] {0};
    session.doWork(
        new Work() {
          @Override
          public void execute(Connection connection) throws SQLException {
            final DatabaseMetaData metaData = connection.getMetaData();
            final String defaultCatalog = helper.getDefaultCatalog();
            final String defaultSchema = helper.getDefaultSchema();

            final ResultSet tableSet =
                metaData.getTables(defaultCatalog, defaultSchema, null, new String[] {"TABLE"});
            try {
              while (tableSet.next()) {
                ctr[0]++;
              }
            } finally {
              tableSet.close();
            }
          }
        });
    return ctr[0];
  }

  @Override
  protected List<String> getDropModifySql(HibernateMigrationHelper helper) {
    return null;
  }

  @Override
  protected List<String> getAddSql(HibernateMigrationHelper helper) {
    return null;
  }

  @Override
  protected Class<?>[] getDomainClasses() {
    return new Class[] {};
  }

  private static class ColMeta {
    public boolean nullable;
    public int size;
    public String name;
  }
}
