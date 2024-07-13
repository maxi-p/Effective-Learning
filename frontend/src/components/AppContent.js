import React, { Suspense, useEffect, useState } from 'react'
import Draggable from 'react-draggable';
import { Navigate, Route, Routes } from 'react-router-dom'
import { CContainer, CSpinner } from '@coreui/react'

import {
  CButton,
  CFormInput,
  CListGroupItem,
  CFormSelect
} from '@coreui/react'

// routes config
import routes from '../routes'

const AppContent = props => {
  const [modules, setModules] = useState([]);
  const [subject, setSubject] = useState({name:'',id: -1});
  const [module, setModule] = useState({name:'', id: -1});
  const [file, setFile] = useState(null);
  

  const handleSubjectSelect = event => {
    const subject = JSON.parse(event.target.value)
    setSubject(
      {
        name: subject.id === -1? '':subject.name,
        id: Number(subject.id)
      }
    );
  }

  useEffect(() => {
      if (subject.id !== -1)
      {
        fetch(`http://localhost:8080/api/v1/file-store/subjects/${subject.id}`, {
          method: "GET",
          headers: {'Authorization':`Bearer ${props.token}`}, 
        })
        .then(res => res.json())
        .then(res => {
          setModules(res)
        });
      }
      else
      {
        setModules([])
        setModule({name: '', id: -1})
      }

  }, [subject])

  const handleModuleSelect = event => {
    const mdl = JSON.parse(event.target.value);
      setModule(
        {
          name: mdl.id === -1? '': mdl.name,
          id: Number(mdl.id)
        }
      );
  }

  const handleFile = event => {
    setFile(event.target.files[0]);
  }

  const handleUpload = () => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('subject', subject.name);
    formData.append('module', module.name);

    fetch(`http://localhost:8080/api/v1/file-store`, {
      method: "POST",
      headers: {'Authorization':`Bearer ${props.token}`}, 
      body: formData
    })
    .then(res => {
      console.log(res)
      setModule({name:'',id: -1})
      setSubject({name:'',id: -1})
      setFile(null)
      props.setIsAddFileMenu(false)
      props.setRefresh(true)
    })
    // .then(res => res.json())
  }

  const handleNewName = event => {
    if (event.target.name === 'newSubject'){
      setSubject({id:-1, name: event.target.value})
    }
    else{
      setModule({id:-1, name: event.target.value})
    }
  }

  return (
    <CContainer className="px-4" lg>
      <Suspense fallback={<CSpinner color="primary" />}>
        {props.isAddFileMenu && (
          <Draggable>
            <div style={{textAlign:'center', width:'500px'}} className="row g-3 areYouSurePopUp">
              <CFormSelect 
                aria-label="Default select example"
                onChange={handleSubjectSelect}
              >
                <option
                  value={JSON.stringify({id: -1, name: 'newSubject'})}
                >
                  New Subject
                </option>
                {props.subjects.map(subject => (
                <option
                  key={subject.id}
                  value={JSON.stringify(subject)}
                >
                  {subject.name}
                </option>))}
              </CFormSelect>
              {subject.id === -1 && (
              <CFormInput
                name='newSubject'
                placeholder='New Subject Name'
                value={subject.name}
                onChange={handleNewName}
                autocomplete='off'
              />)}
              <CFormSelect 
                aria-label="Default select example"
                onChange={handleModuleSelect}
              >
                <option
                  value={JSON.stringify({id: -1, name: 'newModule'})}
                >
                  New Module
                </option>
                {modules.map(mdl => (
                <option
                  key={mdl.id}
                  value={JSON.stringify(mdl)}
                >
                  {mdl.name}
                </option>))}
              </CFormSelect>
              {module.id === -1 && (
              <CFormInput
                name='newModule'
                placeholder='New Module Name'
                value={module.name}
                onChange={handleNewName}
                autocomplete='off'
              />)}
              <CFormInput type="file" onChange={handleFile}/>
              <div
                style={{maxHeight: '100px', overflow:'hidden', overflowY:'scroll'}}
              >
                {/* {listToBeAdded} */}
              </div>
              <div className='cancelDelete'>
                <div className="col-auto" style={{marginRight: '10px'}}>
                  <CButton 
                    color="secondary" 
                    name="cancel" 
                    type="button" 
                    className="mb-3" 
                    onClick={() => props.setIsAddFileMenu(false)}
                  >Cancel
                  </CButton>
                </div>
                <div className="col-auto">
                  <CButton 
                    color="primary" 
                    type="button" 
                    className="mb-3" 
                    onClick={handleUpload}
                  >
                    Add
                  </CButton>
                </div>
              </div>
            </div>
          </Draggable>
        )}
        <Routes>
          {routes.map((route, idx) => {
            return (
              route.element && (
                <Route
                  key={idx}
                  path={route.path}
                  exact={route.exact}
                  name={route.name}
                  element={<route.element 
                      token={props.token}
                      subjects={props.subjects}
                      refresh={props.refresh}
                      setRefresh={props.setRefresh}
                    />}
                />
              )
            )
          })}
          <Route path="/" element={<Navigate to="" replace />} />
        </Routes>
      </Suspense>
    </CContainer>
  )
}

export default React.memo(AppContent)
