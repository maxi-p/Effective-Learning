import { useState, useEffect } from 'react'
import { useParams, useSearchParams } from 'react-router-dom'
import React from 'react'
import DocViewer, { DocViewerRenderers } from "@cyntler/react-doc-viewer";
import CIcon from '@coreui/icons-react';
import axios from 'axios';
import {
    CTable,
    CButton,
  CCardBody,
  CTableHead,
  CTableRow,
  CTableHeaderCell,
  CTableDataCell,
  CTableBody,
  CListGroup,
  CListGroupItem,
} from '@coreui/react'

import {
    cilCaretBottom,
    cilCaretRight,
    cilNotes,
    cilPaperclip,
  } from '@coreui/icons'

const UserFile = props => 
{
    const [url, setUrl] = useState("");
    const {subjectId} = useParams();
    const {moduleId} = useParams();
    const {filename} = useParams();
    
    const docs = 
    [
        { uri: `http://localhost:8080/api/v1/file-store/file/${subjectId}/${moduleId}/${filename}` }, // Local File
    ];

    
useEffect( ()=>{
  fetch("https://www.py4e.com/lectures3/Pythonlearn-01-Intro.pptx")
    .then(res => console.log(res))

  const fetchPdf = async () => {
    try {
        const response = await axios.get(`http://localhost:8080/api/v1/file-store/file/${subjectId}/${moduleId}/${filename}`, {
            responseType: 'blob',
            headers: {
                Authorization: `Bearer ${props.token}`
            }
        });
        const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
        setUrl(url);
        console.log(url)
    } catch (error) {
        console.error('Error fetching the PDF file:', error);
    }
  };

  fetchPdf();

  // fetch(`http://localhost:8080/api/v1/file-store/file/${subjectId}/${moduleId}/${filename}`, {
  //   method: "GET",
  //   headers: {'Authorization':'Bearer '+props.token}, 
  // })
  //   // .then(res => res.body)
  //   .then(res => {
  //     const url = window.URL.createObjectURL(new Blob([res], { type: 'application/pdf' }));
  //     console.log("url ",url)
  //   })
}, [])


    

    const headers = {
        "Authorization": `Bearer ${props.token}`
    };

  return (
    <div>
        {url ? (
           <embed src={url} width="100%" height="500px"></embed>
            // <a href={url} download={filename}>Download PDF</a>
        ) : (
            <p>Loading PDF...</p>
        )}
    </div>
    // <DocViewer documents={docs} prefetchMethod="GET" requestHeaders={headers} pluginRenderers={DocViewerRenderers}/>
  )
}

export default UserFile
