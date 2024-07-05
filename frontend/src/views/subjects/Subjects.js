import { useState, useEffect } from 'react'
import { useParams, useSearchParams } from 'react-router-dom'
import React from 'react'
import {
    CTable,
  CCardBody,
  CTableHead,
  CTableRow,
  CTableHeaderCell,
  CTableDataCell,
  CTableBody,
  CListGroup,
  CListGroupItem,
} from '@coreui/react'

const Subjects = props => {
  const [searchParams] = useSearchParams();
  const [subjects, setSubjects] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/v1/file-store/subjects", {
      method: "GET",
      headers: {'Authorization':'Bearer '+props.token}, 
    })
    .then(res => res.json())
    .then(res => {
        setSubjects(res)
    });
  },[]);

  const subjectList = subjects.map((subject, index) => {
    return  <CTableRow key={index}>
              <CTableHeaderCell scope="row">{index}</CTableHeaderCell>
              <CTableDataCell colSpan={2}><a href={`/#/subjects/${subject.id}`}>{subject.name}</a></CTableDataCell>
              <CTableDataCell>{subject.alias}</CTableDataCell>
            </CTableRow>
  });

  return (
    <CTable small>
      <CTableHead>
        <CTableRow>
          <CTableHeaderCell scope="col">#</CTableHeaderCell>
          <CTableHeaderCell scope="col" colSpan={2}>Subject</CTableHeaderCell>
          <CTableHeaderCell scope="col">Alias</CTableHeaderCell>
        </CTableRow>
      </CTableHead>
      <CTableBody>
        {subjectList}
      </CTableBody>
    </CTable>
  )
}

export default Subjects
