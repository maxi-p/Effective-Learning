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
  CCard,
  CCardHeader,
  CCol,
  CForm,
  CFormInput,
  CFormLabel,
  CFormTextarea,
  CRow,
} from '@coreui/react'

import {
    cilCaretBottom,
    cilCaretRight,
    cilNotes,
    cilPaperclip,
    cilTrash,
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
        console.log(res)
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
                    <CForm style={{textAlign:'center'}} className="row g-3 areYouSurePopUp">
                      <span >Are you sure you want to delete it?</span>
                      <div className='cancelDelete'>
                        <div className="col-auto" style={{marginRight: '10px'}}>
                          <CButton color="secondary" type="button" className="mb-3">
                            Cancel
                          </CButton>
                        </div>
                        <div className="col-auto">
                          <CButton color="danger" type="button" className="mb-3">
                            Delete
                          </CButton>
                        </div>
                      </div>
                    </CForm>
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
                                            <CTableDataCell colSpan={4}><div className="fileOptions"><div className="fileNameDiv"><CIcon icon={cilPaperclip} className="wideIcon"/> <a href={`/#/subjects/${id}/${module.id}/${file.name}?numPages=${file.numberOfPages}`}>{file.name}</a></div><button style={{float:"right",background: 'none',border: 'none'}}><CIcon icon={cilTrash} style={{color:"#db5d5d"}}className="wideIcon"/></button></div></CTableDataCell>
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
