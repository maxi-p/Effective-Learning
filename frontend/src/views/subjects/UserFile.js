import { useState, useEffect } from 'react'
import { useParams, useSearchParams } from 'react-router-dom'
import React from 'react'
import DocViewer, { DocViewerRenderers } from "@cyntler/react-doc-viewer";
import CIcon from '@coreui/icons-react'
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
    
    const {subjectId} = useParams();
    const {moduleId} = useParams();
    const {filename} = useParams();
    
    const docs = 
    [
        { uri: `http://localhost:8080/api/v1/file-store/file/${subjectId}/${moduleId}/${filename}` }, // Local File
    ];

    const headers = {
        "Authorization": `Bearer ${props.token}`
    };

  return (
    <DocViewer documents={docs} prefetchMethod="GET" requestHeaders={headers} pluginRenderers={DocViewerRenderers}/>
  )
}

export default UserFile
