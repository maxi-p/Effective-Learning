import { useState, useEffect } from 'react'
import { useParams, useSearchParams } from 'react-router-dom'
import React from 'react'
import CIcon from '@coreui/icons-react'
import {
    CTable,
    CButton,
  CCardBody,
  CTableHead,
  CTableRow,
  CTableHeaderCell,
  CTableDataCell,
  CTableBody,
  CListGroup,
  CListGroupItem,
} from '@coreui/react'

import {
    cilCaretBottom,
    cilCaretRight,
    cilNotes,
    cilPaperclip,
  } from '@coreui/icons'

const Subjects = props => {
    const api = [
        {
            id: 1,
            name: "C Review",
            files: [
                {
                    id: 1,
                    name: "c-review.pptx"
                },
                {
                    id: 2,
                    name: "FileIO.pptx"
                },
                {
                    id: 3,
                    name: "memoryAddressingPrecision.c"
                },
                {
                    id: 4,
                    name: "pointersArrays.c"
                },
                {
                    id: 5,
                    name: "swap.c"
                },
                {
                    id: 6,
                    name: "arrayPointer.c"
                },
                {
                    id: 7,
                    name: "arrayPointer2.c"
                },
                {
                    id: 8,
                    name: "strings.c"
                },
                {
                    id: 9,
                    name: "intLongPtr.c"
                },

            ]
        },
        {
            id: 2,
            name: "Linked Lists",
            files: [
                {
                    id: 1,
                    name: "LinkedList Data Structure.pptx"
                },
                {
                    id: 2,
                    name: "insertLinkedList.c"
                },
                {
                    id: 3,
                    name: "DoublyLinkedListInsert_Delete.c"
                },
                {
                    id: 4,
                    name: "SinglyLinkedListInsert_Delete.c"
                },
                {
                    id: 5,
                    name: "SortedSinglyLinkedList.c"
                },
            ]
        }
    ]

  const [isVisible, setIsVisible] = useState([true, true]);
  const [searchParams] = useSearchParams();
  const [notes, setNotes] = useState([]);

  const {id} = useParams();
//   console.log(id);

  const toggleVisible = event => {
        console.log("pressed "+event.target.name)
      setIsVisible(prev => prev.map((item, idx) => idx == event.target.name ? !item : item))
  }

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

console.log(isVisible)

  const components = api.map((subject, index) => {
        return <div className="d-grid gap-2" key={index}>
                    <CButton 
                        className="nonRoundButton" 
                        color="secondary"
                        name={index}
                        onClick={toggleVisible}
                    >
                        <CIcon 
                            size="sm" 
                            icon={isVisible[index] ? cilCaretBottom: cilCaretRight} 
                            className="tightIcon"
                        /> {subject.name}
                    </CButton>
                    {isVisible[index] && <CTable small>
                        <CTableBody>
                            {subject.files.map((file, index) => {
                                return  <CTableRow key={index}>
                                            <CTableDataCell colSpan={4}><CIcon icon={cilPaperclip} className="wideIcon"/> <a href={"/#/subjects/1/files/"+file.id}>{file.name}</a></CTableDataCell>
                                        </CTableRow>
                            })}
                        </CTableBody>
                    </CTable>}
                </div>
  })

  return (
    <div className="d-grid gap-2">
        {components}
    </div>
  )
}

export default Subjects
