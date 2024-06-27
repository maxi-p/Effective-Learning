import React, { useEffect, useState } from 'react'
import {
    CRow,
  CCol,
  CCard,
  CCardBody,
  CCardImage,
  CCardTitle,
  CButton,
} from '@coreui/react'

const Categories = props => {
  const [categories, setCategories] = useState([]);
  const [categoryList, setCategoryList] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/v1/note-category", {
      method: "GET",
      headers: {'Authorization':'Bearer '+props.token}, 
    })
    .then(res => res.json())
    .then(res => {
      setCategories(res)
    });
  },[]);

  useEffect(()=>
  {
    var list = [];
    var subList = [];

    for (let i=0; i<categories.length; i++)
    {
      subList.push(<CCol lg={4} key={i}>
        <CCard 
          style={{ width: '18rem' , marginTop: '15px'}}
          key={i}>
            <CButton
              size="lg"
              value={categories[i].name}
              key={i}
              href={"/#/notes/dictionary?category="+categories[i].name.replace(" ", "%20")}>{categories[i].name}
            </CButton>
        </CCard>
      </CCol>)  
      if((i+1)%3===0)
      {
        list.push(<CRow key={i}>{subList}</CRow>);
        subList = [];
      }
    }
    if(subList != [])
    {
      list.push(<CRow key={categories.length}>{subList}</CRow>);
    }
    setCategoryList(list);
  },[categories]);
  
  return (<div>
            {categoryList}
          </div>)
}

export default Categories
