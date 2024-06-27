import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
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
  CDropdown,
  CDropdownToggle,
  CDropdownMenu,
  CDropdownItem
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import { cilChevronBottom, cilChevronTop } from '@coreui/icons'

const Generate = props => {
  const [queryString, setQueryString] = useState("");
  const [focused, setFocused] = useState(false);
  const [cart, setCart] = useState([]);
  
  // API call
  const [listOfCategories, setListOfCategories] = useState([]);
  const [ids, setIds] = useState([]);
  const [count, setCount] = useState(1);

  const navigate = useNavigate();

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

  const filteredItems = listOfCategories.filter(category => {
    return category.name.toLocaleLowerCase().includes(queryString.toLocaleLowerCase());
  });

  const handleAddCart1 = event => {
    const name = event.target.value;
    const id = event.target.name;
    console.log(id)
    setCart(prev => {
      return [...prev, name]
    });
    setIds(prev => {
      return [...prev, id]
    })
  };

  const handleQuery = event => {
    setQueryString(event.target.value)
  };

  const handleFocus = () => {
    setFocused(prev => {
      return !prev
    })
  };

  const upCount = () => {
    setCount(prev => {
      if (prev === "")
      {
        return 1;
      }
      else{
        return prev+1
      }
    })
  };

  const downCount = () => {
    setCount(prev => {
      if (prev === "" || prev === 1){
        return 1
      }
      else
      {
        return prev-1
      }
    })
  };

  const updateCount = event => {
    if (event.target.value === ""){
      setCount("")
    }
    else {
      setCount(parseInt(event.target.value,10))
    }
  };

  const buttons = filteredItems.map((category,index) => {
    return  <CButton 
                className="leftText" 
                color="dark" 
                type="button"
                name={category.id}
                key={category.id}
                value={category.name}
                onClick={handleAddCart1}
              >
                {category.name}
              </CButton>
  });

  const cartItems = cart.map((item, index) => {
    return <CListGroupItem key={index}>* {item}</CListGroupItem>
  });

  const handleGenerate = event => {
    const data = 
    {
      questionNumber: count,
      categories: ids
    }

    fetch("http://localhost:8080/api/v1/note-test/order-test", {
      method: "POST",
      headers: {'Content-Type': 'application/json', 'Authorization':'Bearer '+props.token}, 
      body: JSON.stringify(data)
    })
    .then(res => res.json())
    .then(res => {
      console.log("Test generated! response:", res);
      navigate('/training/tests?testId='+res.id);
    });
  }

  console.log(filteredItems)
  console.log(ids)
  
  return (
    <CCard className="mb-4">
        <CForm className="row g-3"> 
            <CCardBody>
                <CFormInput
                  type="text"
                  placeholder="Enter category"
                  aria-label="readonly input example"
                  onChange={handleQuery}
                  onClick={handleFocus}
                  value={queryString}
                />

                 <CButtonGroup style={{display:focused?"block":"none"}} vertical aria-label="Vertical button group">
                  {buttons}
                </CButtonGroup>
            </CCardBody>
            
            <CCardBody>
                <CListGroup>
                  {cartItems}
                </CListGroup>
            </CCardBody>
            <CCardBody>
                <CInputGroup className="mb-3">
                  <CButton 
                    type="button" 
                    color="secondary" 
                    variant="outline"
                    onClick={upCount}>
                    <CIcon icon={cilChevronTop}/>
                  </CButton>
                  <CButton 
                    type="button" 
                    color="secondary" 
                    variant="outline"
                    onClick={downCount}>
                    <CIcon icon={cilChevronBottom}/>
                  </CButton>
                  <CFormInput 
                    value={count} 
                    onChange={updateCount} />
                </CInputGroup>
            </CCardBody>
            <CCardBody>
                  <CButton 
                    color="primary" 
                    type="button" 
                    onClick={handleGenerate} 
                    className="mb-3"
                  >Generate Test
                  </CButton>
            </CCardBody>
        </CForm>
    </CCard>
  )
}

export default Generate
