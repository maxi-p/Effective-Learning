import React, { useState, useEffect } from 'react';
import { useParams, useSearchParams } from 'react-router-dom'

import PDFViewer from './PDFViewer'

const File = props => {
    const [searchParams] = useSearchParams();
    const resultPage = searchParams.get("resultPage");
    
    const [pdfUrl, setPdfUrl] = useState('');
    const [currentPage, setCurrentPage] = useState(resultPage? Number(resultPage):1);
    const [add, setAdd] = useState(true);
    const [inputActive, setInputActive] = useState(false);

    const {subjectId} = useParams();
    const {moduleId} = useParams();
    const {filename} = useParams();

    const activate = () =>{
      console.log('activating')
      setInputActive(true);
    }

    const deActivate = () =>{
      console.log('deActivating')
      setInputActive(false);
    }

    useEffect(()=>{
        // Loadign first Page
      fetch(`http://localhost:8080/api/v1/file-store/file/${subjectId}/${moduleId}/${filename}`, {
        method: "GET",
        headers: {'Authorization':`Bearer ${props.token}`}, 
      })
      .then(res => {
        if(res.status !=200) 
          alert(res.status)
        return res;
      })
      .then((response) => response.blob())
      .then((blob) => URL.createObjectURL(blob))
      .then(link => setPdfUrl(link))
    },[])

    return (
        <div style={{width: '100%'}}>
            <PDFViewer 
                pdfUrl={pdfUrl} 
                filename={filename}
                moduleId={moduleId}
                subjectId={subjectId}
                token={props.token}
                currentPage={currentPage}
                activate={activate}
                deActivate={deActivate}
                setCurrentPage={setCurrentPage}
                add={add}
                setAdd={setAdd}
                inputActive={inputActive}
            />
        </div>
    );
}

export default File;
