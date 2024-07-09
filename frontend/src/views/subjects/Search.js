import { useState, useEffect } from 'react'
import { useParams, useSearchParams } from 'react-router-dom'
import React from 'react'
import Draggable from 'react-draggable';
import CIcon from '@coreui/icons-react'
import {
  CTable,
  CFormInput,
  CTableRow,
  CTableDataCell,
  CTableBody,
} from '@coreui/react'

import {
    cilPaperclip,
  } from '@coreui/icons'

const Search = props => 
{
  const [files, setFiles] = useState([]);
  const [inpt, setInput] = useState('');

  const handleKey = event => {
    if (event.code === 'Enter' && inpt !== '')
    {
        fetch(`http://localhost:8080/api/v1/file-store/search-hint/${inpt}`, {
            method: "GET",
            headers: {'Authorization':'Bearer '+props.token}, 
        })
        .then(res => res.json())
        .then(res => {
            setFiles(res)
        });
    }
  }
  
  const handleChange = event => {
      setInput(event.target.value)
  }

  const components = files.map((file, indx) => {
        return  <CTableRow key={indx}>
                    <CTableDataCell colSpan={4}>
                        <div className="fileOptions">
                            <div className="fileNameDiv">
                                <CIcon icon={cilPaperclip} className="wideIcon"/> 
                                <a href={`/#/subjects/${file.subjectId}/${file.moduleId}/${file.name}?numPages=${file.numberOfPages}&resultPage=${file.resultPage}`}>PAGE {file.resultPage} - {file.name}</a>
                            </div>
                        </div>
                    </CTableDataCell>
                </CTableRow>
    })

  return (
    <div className="d-grid gap-2">
        <CFormInput
            type="text"
            style={{width: '100%'}}
            onKeyDown={handleKey}
            onChange={handleChange}
            value={inpt}
        />
        <CTable small>
            <CTableBody>
                {components}
            </CTableBody>
        </CTable>
    </div>
  )
}

export default Search
