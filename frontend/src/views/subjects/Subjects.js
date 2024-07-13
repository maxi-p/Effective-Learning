import { useState, useEffect } from 'react'
import { useParams, useSearchParams } from 'react-router-dom'
import React from 'react'
import {
    CTable,
  CTableHead,
  CTableRow,
  CTableHeaderCell,
  CTableDataCell,
  CTableBody,
} from '@coreui/react'

const Subjects = props => {

  const subjectList = props.subjects.map((subject, index) => {
    return  <CTableRow key={index}>
                <CTableHeaderCell scope="row">{index}</CTableHeaderCell>
                <CTableDataCell colSpan={2}><a href={`/#/subjects/${subject.id}`}>{subject.name}</a></CTableDataCell>
                <CTableDataCell>{subject.alias}</CTableDataCell>
            </CTableRow>
  });

  return (
    <div>
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
    </div>
  )
}

export default Subjects
