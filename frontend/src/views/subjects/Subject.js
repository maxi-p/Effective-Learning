import { useState, useEffect } from 'react'
import { useParams, useSearchParams } from 'react-router-dom'
import React from 'react'
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

const Subject = props => {

  const [isVisible, setIsVisible] = useState([]);
  const [modules, setModules] = useState([]);

  const {id} = useParams();

  const toggleVisible = event => {
      setIsVisible(prev => prev.map((item, idx) => idx == event.target.name ? !item : item))
  }

  useEffect(() => {
    fetch(`http://localhost:8080/api/v1/file-store/subjects/${id}`, {
      method: "GET",
      headers: {'Authorization':'Bearer '+props.token}, 
    })
    .then(res => res.json())
    .then(res => {
        setModules(res)
    });
  },[]);

  useEffect(() => {
    var bools = []
    modules.map(module => {
        bools.push(true)
    });
    setIsVisible(bools)
  },[modules]);

  const components = modules.map((module, index) => {
        return <div className="d-grid gap-2" key={index}>
                    <CButton 
                        className="nonRoundButton" 
                        color="secondary"
                        name={index}
                        onClick={toggleVisible}
                    >
                        <CIcon 
                            size="sm" 
                            icon={isVisible[index] ? cilCaretBottom: cilCaretRight} 
                            className="tightIcon"
                        /> {module.name}
                    </CButton>
                    {isVisible[index] && <CTable small>
                        <CTableBody>
                            {module.files.map((file, index) => {
                                return  <CTableRow key={index}>
                                            <CTableDataCell colSpan={4}><CIcon icon={cilPaperclip} className="wideIcon"/> <a href={`/#/subjects/${id}/${module.id}/${file.name}`}>{file.name}</a></CTableDataCell>
                                        </CTableRow>
                            })}
                        </CTableBody>
                    </CTable>}
                </div>
  })

  return (
    <div className="d-grid gap-2">
        {components}
    </div>
  )
}

export default Subject
