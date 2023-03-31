import React from 'react';
import styled from 'styled-components';
import TreeRecursive from './TreeRecursive';

interface TreeProps {
    data: any;
}

const StyledTree = styled.div`
    line-height: 1.5;
`;

const Tree: React.FC<TreeProps> = ({ data }) => {

    return (
        <StyledTree>
            <TreeRecursive data={data} />
        </StyledTree>
    )
};

export default Tree;