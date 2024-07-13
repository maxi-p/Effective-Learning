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
    const [searchParams] = useSearchParams();
    const numPages = searchParams.get("numPages");
    const resultPage = searchParams.get("resultPage");

    const [page, setPage] = useState(resultPage?Number(resultPage):1);
    const [ready, setReady] = useState(false);
    const [imageUrls, setImageUrls] = useState([]);
    const [inpt, setInput] = useState('');
    const [toBeAdded, setToBeAdded] = useState([]);
    const [add, setAdd] = useState(true);

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
      const firstPage = resultPage?Number(resultPage)-1:0;

      // Loadign first Page
      fetch(`http://localhost:8080/api/v1/file-store/images/${subjectId}/${moduleId}/${nameWithoutExtension}_page-${firstPage}.jpg`, {
        method: "GET",
        headers: {'Authorization':'Bearer '+props.token}, 
      })
      .then(res => {
        if(res.status !=200) 
          alert(res.status)
        return res;
      })
      .then((response) => response.blob())
      .then((blob) => URL.createObjectURL(blob))
      .then((url) => {setImageUrls(prev=>prev.map((item, index) => index == firstPage ? url : item))})
      .then(() => {setReady(true)})
      .catch(err => window.alert(err))

      for (let i=0; i<numPages; i++ )
      {
        if (i!=firstPage)
        {
          fetch(`http://localhost:8080/api/v1/file-store/images/${subjectId}/${moduleId}/${nameWithoutExtension}_page-${i}.jpg`, {
            method: "GET",
            headers: {'Authorization':'Bearer '+props.token}, 
          })
          .then(res => {
            if(res.status !=200) 
              alert(res.status)
            return res;
          })
          .then((response) => response.blob())
          .then((blob) => URL.createObjectURL(blob))
          .then((url) => {setImageUrls(prev=>prev.map((item, index) => index == i ? url : item))})
          .catch(err => window.alert(err))
        }
      }  
    }, []);

    useEffect(() =>{
      const keyDownHandler = e => {
        if (e.key === 'ArrowRight')
          incrementPage();
        if (e.key === 'ArrowLeft')
          decrementPage();
      }

      document.addEventListener("keydown", keyDownHandler);
      return () => {
        document.removeEventListener("keydown", keyDownHandler);
      };
    },[]);

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
        if(res.status !=200) 
          alert(res.status)
        return res;
      })
      .catch(err => window.alert(err))

      setToBeAdded([]);
    }

    const incrementPage = () => {
      setPage(prev => prev==numPages? 1 :(prev+1)%(numPages+1))
    }

    const decrementPage = () => {
      setPage(prev => prev==1? Number(numPages): (prev-1)%(numPages+1))
    }

    const handleSlide = event => {
      if (event.target.name === 'next')
        incrementPage();
      else
        decrementPage();
    }

    const handleKey = event => {
      console.log(event.code)
      if (event.code === 'Enter' && inpt !== '')
      {
        setToBeAdded(prev => [
          inpt, ...prev
        ])
        setInput('')   
      }
    }
    
    const handleAdd = event => {
      if (event.target.name === "cancel")
        setToBeAdded([])
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
          <div style={{width:'1200px'}}>
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
            <button name="prev" onClick={handleSlide}>Prev</button>
            <button name="next" onClick={handleSlide}>Next</button>
            <span>  {page}/{numPages}</span>
            <button onClick={(handleAdd)} name="add" style={{float:"right"}}>Add</button>
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
