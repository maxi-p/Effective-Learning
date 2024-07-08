import React, { useState, useEffect } from 'react';
import { useParams, useSearchParams } from 'react-router-dom'
import Draggable from 'react-draggable';
import {
  CForm,
  CButton,
  CFormInput,
  CListGroup,
  CListGroupItem
} from '@coreui/react'

const UserFile = props => {
    const [page, setPage] = useState(1);
    const [ready, setReady] = useState(false);
    const [imageUrls, setImageUrls] = useState([]);
    const [searchParams] = useSearchParams();
    const [inpt, setInput] = useState('');
    const [toBeAdded, setToBeAdded] = useState([]);
    const [add, setAdd] = useState(false);

    const numPages = searchParams.get("numPages");

    const {subjectId} = useParams();
    const {moduleId} = useParams();
    const {filename} = useParams();

    useEffect(() => {
      const nameWithoutExtension = filename.replace(".pdf", "")
      const arr = []
      for (let i=0; i<numPages; i++)
      {
        arr.push("")
      }
      setImageUrls(arr);
      for (let i=0; i<numPages; i++ )
      {
        fetch(`http://localhost:8080/api/v1/file-store/images/${subjectId}/${moduleId}/${nameWithoutExtension}_page-${i}.jpg`, {
          method: "GET",
          headers: {'Authorization':'Bearer '+props.token}, 
        })
        .then((response) => response.blob())
        .then((blob) => URL.createObjectURL(blob))
        .then((url) => {setImageUrls(prev=>prev.map((item, index) => index == i ? url : item))})
        .then(() => {if(i == 0) setReady(true)})
      }  
    }, []);

    const handleSubmit = event => {
      console.log(filename)
      const data = {
        fileName: filename,
        moduleId: moduleId,
        subjectId: subjectId,
        pageNumber: page,
        hints: toBeAdded
      }
      fetch(`http://localhost:8080/api/v1/file-store/search-hint`, {
        method: "POST",
        headers: {
          'Authorization':'Bearer '+props.token,
          'Content-Type': 'application/json'
        }, 
        body: JSON.stringify(data)
      })
      .then(res => {
        console.log(res)
      })

      setAdd(false);
      setToBeAdded([]);
    }

    const handleSlide = event => {
      setPage(prev => event.target.name === 'next'? prev==numPages? 1: (prev+1)%(numPages+1): prev===1? Number(numPages): (prev-1)%(numPages+1))
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
    
    const handleAdd = event => {
      setAdd(prev => !prev)
    }

    const handleChange = event => {
        setInput(event.target.value)
    }

    const listToBeAdded = toBeAdded.map((item, index) => <CListGroupItem key={index}>{item}</CListGroupItem>
    )

    return (
        <div>
          {ready && (
          <div style={{width:'800px'}}>
            {add && <Draggable>
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
                >
                  {listToBeAdded}
                </div>
                <div className='cancelDelete'>
                  <div className="col-auto" style={{marginRight: '10px'}}>
                    <CButton color="secondary" type="button" className="mb-3" onClick={handleAdd}>
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
            <button name="prev" onClick={handleSlide}>Prev</button>
            <button name="next" onClick={handleSlide}>Next</button>
            <span>  {page}/{numPages}</span>
            <button onClick={handleAdd} style={{float:"right"}}>Add</button>
            <br/>
              {imageUrls.map((imageUrl, index) => (
                <img key={index} style={index===(page-1)?{display: 'block',width:'100%'}:{display: 'none'}} src={imageUrl} alt={`Slide ${index+1}`} />
              ))}
          </div>)
          }
        </div>
    );
}

export default UserFile;
