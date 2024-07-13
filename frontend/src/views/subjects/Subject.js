import { useState, useEffect } from 'react'
import { useParams, useSearchParams } from 'react-router-dom'
import React from 'react'
import Draggable from 'react-draggable';
import CIcon from '@coreui/icons-react'
import {
    CTable,
    CButton,
    CTableRow,
    CTableDataCell,
    CTableBody,
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
  const [remove, setRemove] = useState(false);
  const [toRemove, setToRemove] = useState({subjectId:'', moduleId:'', filename:''})

  const {id} = useParams();

  const toggleVisible = event => {
      setIsVisible(prev => prev.map((item, idx) => idx == event.target.name ? !item : item))
  }

  useEffect(() => {
      fetch(`http://localhost:8080/api/v1/file-store/subjects/${id}`, {
        method: "GET",
        headers: {'Authorization':`Bearer ${props.token}`}, 
      })
      .then(res => res.json())
      .then(res => {
        setModules(res)
        props.setRefresh(false)
      });
  },[props.refresh]);

  useEffect(() => {
    var bools = []
    modules.map(module => {
        bools.push(true)
    });
    setIsVisible(bools)
  },[modules]);

  const handleDelete = ({subjectId, moduleId, filename}) => {
    setToRemove({subjectId: subjectId, moduleId: moduleId, filename: filename})
    setRemove(true);
  }

  const handleCancel = event => {
    setToRemove({subjectId:'', moduleId:'', filename:''});
    setRemove(false);
  }

  const deleteFile = () => {
    fetch(`http://localhost:8080/api/v1/file-store/file/${toRemove.subjectId}/${toRemove.moduleId}/${toRemove.filename}`, {
      method: "DELETE",
      headers: {'Authorization':`Bearer ${props.token}`}, 
    })
    .then(res => console.log(res.json()))
    .then(() => {
      props.setRefresh(true)
      setToRemove({subjectId:'', moduleId:'', filename:''});
      setRemove(false);
    });
  }

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
                            {module.files.map((file, indx) => {
                                return  <CTableRow key={indx}>
                                            <CTableDataCell colSpan={4}>
                                                <div className="fileOptions">
                                                    <div className="fileNameDiv">
                                                        <CIcon icon={cilPaperclip} className="wideIcon"/> 
                                                        <a href={`/#/subjects/${id}/${module.id}/${file.name}`}>{file.name}</a>
                                                    </div>
                                                    <button 
                                                        onClick={() => handleDelete({subjectId: id, moduleId: module.id, filename: file.name})}
                                                        style={{float:"right",background: 'none',border: 'none'}} 
                                                    >
                                                        <CIcon icon={cilTrash} style={{color:"#db5d5d"}}className="wideIcon"/>
                                                    </button>
                                                </div>
                                            </CTableDataCell>
                                        </CTableRow>
                            })}
                        </CTableBody>
                    </CTable>}
                </div>
  })

  return (
    <div className="d-grid gap-2">
        {remove && <Draggable>
            <div style={{textAlign:'center'}} className="row g-3 areYouSurePopUp">
              <span >Are you sure you want to delete {toRemove.filename}?</span>
              <div className='cancelDelete'>
                <div className="col-auto" style={{marginRight: '10px'}}>
                  <CButton 
                    color="secondary" 
                    type="button" 
                    className="mb-3" 
                    onClick={handleCancel}
                  >
                    Cancel
                  </CButton>
                </div>
                <div className="col-auto">
                  <CButton 
                    color="danger" 
                    type="button" 
                    className="mb-3"
                    onClick={deleteFile}
                  >
                    Delete
                  </CButton>
                </div>
              </div>
            </div>
        </Draggable>}
        {components}
    </div>
  )
}

export default Subject
