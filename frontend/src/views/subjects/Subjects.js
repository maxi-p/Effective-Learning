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
  const [notes, setNotes] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/v1/note", {
      method: "GET",
      headers: {'Authorization':'Bearer '+props.token}, 
    })
    .then(res => res.json())
    .then(res => {
      setNotes(res)
    });
  },[]);

  const notesList = notes.filter(note => {
    return !searchParams.get('category') || note.noteCategory.name.toLowerCase().includes(searchParams.get('category').toLowerCase())
  })

  const res = notesList.map(note => {
    return <CListGroup 
              className="mb-2" 
              layout={`horizontal`} 
              key={note.id}>
              <CListGroupItem>{note.key}</CListGroupItem>
              <CListGroupItem>{note.value}</CListGroupItem>
            </CListGroup>
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
      <CTableRow>
        <CTableHeaderCell scope="row">1</CTableHeaderCell>
        <CTableDataCell colSpan={2}><a href="/#/subjects/1">Algorithms and Data Structures 1</a></CTableDataCell>
        <CTableDataCell>COP3502</CTableDataCell>
      </CTableRow>
      <CTableRow>
        <CTableHeaderCell scope="row">2</CTableHeaderCell>
        <CTableDataCell colSpan={2}><a href="/#/subjects/2">Algorithms and Data Structures 2</a></CTableDataCell>
        <CTableDataCell>COP3503</CTableDataCell>
      </CTableRow>
      <CTableRow>
        <CTableHeaderCell scope="row">3</CTableHeaderCell>
        <CTableDataCell colSpan={2}><a href="/#/subjects/3">LeetCode Coding Problems</a></CTableDataCell>
        <CTableDataCell>N/A</CTableDataCell>
      </CTableRow>
    </CTableBody>
  </CTable>
  )
}

export default Subjects
