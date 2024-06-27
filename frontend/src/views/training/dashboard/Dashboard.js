import React, { useEffect, useState } from 'react'
import {
  CRow,
  CCol,
  CWidgetStatsB,
  CCard,
  CCardHeader,
  CCardBody,
  CCardTitle,
  CCardText,
  CButton,
  CNav,
  CNavItem,
  CNavLink
} from '@coreui/react'


const Dashboard = props => {
  const [results, setResults] = useState([]);
  const [resultList, setResultList] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/v1/note-test/result", {
      method: "GET",
      headers: {'Authorization':`Bearer ${props.token}`}, 
    })
    .then(res => res.json())
    .then(res => {
      setResults(res)
      console.log(res)
    });
  },[]);

  useEffect(()=>
    {
      var list = [];
      var subList = [];
  
      for (let i=0; i<results.length; i++)
      {
        const percentage = parseInt(results[i].correctQuestionNumber)*100/parseInt(results[i].questionNumber)
        subList.push(<CCol lxs={12} sm={6} xl={6} xxl={3} key={i}>
          <CWidgetStatsB
                style={{marginBottom:"20px"}}
                key={i}
                progress={{ color: 'success', value: percentage }}
                text={`${results[i].correctQuestionNumber}/${results[i].questionNumber}`}
                value={results[i].testName}
                title={`${percentage.toFixed(1)}%`}
              />
          {/* <CCard 
            style={{ width: '18rem' , marginTop: '15px'}}
            key={i}>
          </CCard> */}
        </CCol>)  
        if((i+1)%5===0)
        {
          list.push(<CRow key={i}>{subList}</CRow>);
          subList = [];
        }
      }
      if(subList != [])
      {
        list.push(<CRow key={results.length}>{subList}</CRow>);
      }
      setResultList(list);
    },[results]);

  return (<div>
            {resultList}
          </div>)
  return (
      <CRow>
        <CCol xs={12} sm={6} xl={4} xxl={3}>
        <CWidgetStatsB
                progress={{ color: 'success', value: 89.9 }}
                text="36/40"
                title="Training 1"
                value="90.0%"
              />
        </CCol>
      </CRow>
        )
}

export default Dashboard
