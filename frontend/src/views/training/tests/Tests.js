import React, { useEffect, useState } from 'react'
import {
  CCol,
  CRow,
  CCard,
  CCardImage,
  CCardBody,
  CListGroup,
  CCardLink,
  CListGroupItem,
} from '@coreui/react'

import ReactImg from 'src/assets/images/react.jpg'

const ListGroups = props => {
  const [tests, setTests] = useState([]);
  const [testList, setTestList] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/v1/note-test", {
      method: "GET",
      headers: {'Authorization':'Bearer '+props.token}, 
    })
    .then(res => res.json())
    .then(res => {
      setTests(res)
    });
  },[]);

  useEffect(()=>
    {
      var list = [];
      var subList = [];
  
      for (let i=0; i<tests.length; i++)
      {
        subList.push
        (<CCol lg={4} key={i}>
          <CCard 
            style={{ width: '18rem' , marginBottom: '30px'}}
            key={i}>
              <CCardImage orientation="top" src={ReactImg} />
              <CListGroup flush>
                <CListGroupItem key="1">Test {i+1}</CListGroupItem>
              </CListGroup>
              <CCardBody>
                <CCardLink href={`/#/training/tests?testId=${tests[i].id}&testName=Test ${i+1}`}>Take</CCardLink>
                <CCardLink href="#">Delete</CCardLink>
              </CCardBody>
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
        list.push(<CRow key={tests.length}>{subList}</CRow>);
      }
      setTestList(list);
    },[tests]);

  return (<div>
            {testList}
          </div>)
}

export default ListGroups
