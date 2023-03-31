import Tree from './Tree'
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const MyPage = () => {
    const [ structureData, setStructureData ] = useState<any>();
    const client = axios.create({
        baseURL: 'http://localhost:8080/vanillanew/api/',
        headers : {
            'Content-Type' : 'application/json',
            Accept: 'application/json'
        }
    })

    const getDataApi = async () => {
       return client.get('browsehierarchy/all').then((response: any) => response.data);
    }

    const setDataApi = async () => {
        const details = await getDataApi();
        setStructureData(details)
    }

    useEffect(() => {
        setDataApi();
    },[])

    return (
        <div>
            <Tree data={structureData ?? []} />
        </div>
    )
    }

export default MyPage;
 