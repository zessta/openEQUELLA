import React, { useState } from 'react';
import styled from 'styled-components';

interface FolderProps {
    name: any;
    children: any;
}

const StyledFolder = styled.div`
  padding-left: 20px;
  && .folder-label {
    display: flex;
    align-items: center;
  }
  && .folder-label span {
      margin-left: 5px;
    }
`;

const Collapsible = styled.div<{ $isOpen: boolean}>`
    height: ${props => (props.$isOpen ? 'auto' : '0px')};
    overflow: hidden;
`;

const Folder: React.FC<FolderProps> = ({ name, children }) => {
    const [isOpen, setIsOpen] = useState<boolean>(false);

    const handleToggle = (event: any) => {
        event.preventDefault();
        setIsOpen(!isOpen);
    };

    return (
        <StyledFolder>
            <div className="folder-label" onClick={handleToggle}>
                <span>{name}</span>
            </div>
            <Collapsible $isOpen={isOpen}>{children}</Collapsible>
        </StyledFolder>
    );
};

export default Folder;
