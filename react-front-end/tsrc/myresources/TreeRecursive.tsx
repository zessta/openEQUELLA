import React from 'react';
import Folder from './Folder';

interface TreeRecursiveProps {
    data: any
}

const TreeRecursive: React.FC<TreeRecursiveProps> = ({
    data
}) => {
   return (
       data && (data?.length > 0) && data?.map((item: any) => {
       return <Folder name={item?.value}>
           {item?.dao && (item?.dao?.length > 0) && <TreeRecursive data={item?.dao} />}
       </Folder>
    })
   ); 
};

export default TreeRecursive;