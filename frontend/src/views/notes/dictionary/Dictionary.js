import { useState, useEffect } from 'react'
import { useSearchParams } from 'react-router-dom'
import React from 'react'
import {
  CCardBody,
  CListGroup,
  CListGroupItem,
} from '@coreui/react'

const ListGroups = props => {
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
        <CCardBody>
          {res}
        </CCardBody>
  )
}

export default ListGroups
