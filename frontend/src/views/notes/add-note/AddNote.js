import React, { useState, useEffect } from 'react'
import {
  CCard,
  CCardHeader,
  CCardBody,
  CFormInput,
  CListGroup,
  CListGroupItem,
  CInputGroup,
  CButton,
  CForm,
  CButtonGroup,
} from '@coreui/react'
import { useNavigate, Link } from 'react-router-dom';

const Generate = props => {
  const [note, setNote] = useState({key:"", value:""})
  const [queryString, setQueryString] = useState("");
  const [focused, setFocused] = useState(false);
  // API call
  const [listOfCategories, setListOfCategories] = useState([]);
  const [category, setCategory] = useState("");

  useEffect(() => {
    fetch("http://localhost:8080/api/v1/note-category", {
      method: "GET",
      headers: {'Authorization':'Bearer '+props.token}, 
    })
    .then(res => res.json())
    .then(res => {
      setListOfCategories(res)
    });
  },[]);

  const navigate = useNavigate();

  const filteredItems = listOfCategories.filter(category => {
    return category.name.toLocaleLowerCase().includes(queryString.toLocaleLowerCase());
  });

  const handleSelectCategory = event => {
    setFocused(false);
    setQueryString("")
    setCategory(event.target.value);
  };

  const handleQuery = event => {
    setQueryString(event.target.value)
  };

  const focusOn = () => {
    setFocused(true)
  };

  const handleNote = event => {
    const {name, value} = event.target
    setNote(prev => {
        return {
            ...prev,
            [name]: value
        }
    })
  }
  const buttons = filteredItems.map((category,index) => {
    return  <CButton 
              className="leftText" 
              color="dark" 
              type="button"
              key={index}
              value={category.name}
              onClick={handleSelectCategory}
            >{category.name}
            </CButton>
  });

  const handleSubmit = () => 
  {
    const data = 
    {
      key: note.key,
      value: note.value,
      noteCategory: 
      {
        name: category
      }
    }

    fetch("http://localhost:8080/api/v1/note", {
      method: "POST",
      headers: {'Content-Type': 'application/json', 'Authorization':'Bearer '+props.token}, 
      body: JSON.stringify(data)
    }).then(res => {
      console.log("Request complete! response:", res);
      navigate('/notes/dictionary');
    });
  };

  return (
    <CCard className="mb-4">
      <CForm className="row g-3"> 
        <CCardBody>
          <CFormInput
            type="text"
            placeholder="Enter category"
            aria-label="readonly input example"
            onChange={handleQuery}
            onFocus={focusOn}
            value={queryString}
          />
          <CButtonGroup 
            style={{display:focused?"block":"none"}} 
            vertical aria-label="Vertical button group"
          >{buttons}
          </CButtonGroup>
        </CCardBody>
        <CCardBody>
          <CListGroup>
            <CListGroupItem >{category}</CListGroupItem>
          </CListGroup>
        </CCardBody>
        <CCardBody>
          <CInputGroup className="mb-3">
            <CFormInput 
              value={note.key}
              name="key" 
              placeholder='Enter Key'
              onChange={handleNote} 
            />
            <CFormInput 
              value={note.value}
              name="value" 
              placeholder='Enter Value'
              onChange={handleNote} 
            />
          </CInputGroup>
        </CCardBody>
        <CCardBody>
          <CButton 
            color="primary" 
            type="button" 
            onClick={handleSubmit} 
            className="mb-3"
          >Add Note
          </CButton>
        </CCardBody>
      </CForm>
    </CCard>)
}

export default Generate
