import Draggable from 'react-draggable';
import React, { useState, useEffect } from 'react';

import {
    CButton,
    CFormInput,
    CListGroupItem
  } from '@coreui/react'

const HintField = props => {
    const [inpt, setInput] = useState('');
    const [toBeAdded, setToBeAdded] = useState([]);

    const handleSubmit = event => {
        const data = {
            fileName: props.filename,
            moduleId: props.moduleId,
            subjectId: props.subjectId,
            pageNumber: props.currentPage,
            hints: toBeAdded
        }
        console.log(data)
        fetch(`http://localhost:8080/api/v1/file-store/search-hint`, {
            method: "POST",
            headers: {
                'Authorization':`Bearer ${props.token}`,
                'Content-Type': 'application/json'
            }, 
            body: JSON.stringify(data)
        })
        .then(res => {
            if(res.status !=200) 
                alert(res.status)
            return res;
        })
        .then(res => console.log(res))
        .catch(err => window.alert(err))
  
        setToBeAdded([]);
    }

    const handleKey = event => {
        if (event.code === 'Enter' && inpt !== '')
        {
            setToBeAdded(prev => [
                inpt, ...prev
            ])
            setInput('')   
        }
    }
    
    const handleChange = event => {
        setInput(event.target.value)
    }

    const handleAdd = event => {
        if (event.target.name === "cancel")
            setToBeAdded([])
        props.setAdd(prev => !prev)
    }

    const listToBeAdded = toBeAdded.map((item, index) => <CListGroupItem key={index}>{item}</CListGroupItem>)

    return (
        <div>{props.add && 
            <Draggable>
                <div style={{textAlign:'center', width:'500px'}} className="row g-3 areYouSurePopUp">
                    <CFormInput
                        type="text"
                        style={{width: '100%'}}
                        onKeyDown={handleKey}
                        onChange={handleChange}
                        value={inpt}
                    />
                    <div
                        style={{maxHeight: '100px', overflow:'hidden', overflowY:'scroll'}}
                    >{listToBeAdded}
                    </div>
                    <div className='cancelDelete'>
                        <div className="col-auto" style={{marginRight: '10px'}}>
                            <CButton color="secondary" name="cancel" type="button" className="mb-3" onClick={handleAdd}>
                              Cancel
                            </CButton>
                        </div>
                        <div className="col-auto">
                            <CButton color="primary" type="button" className="mb-3" onClick={handleSubmit}>
                              Save
                            </CButton>
                        </div>
                    </div>
                </div>
            </Draggable>}
        </div>)
}

export default HintField
