import React from 'react'

// File
const Subjects  = React.lazy(() => import('./views/subjects/Subjects'))
const Subject   = React.lazy(() => import('./views/subjects/Subject'))
const Search    = React.lazy(() => import('./views/subjects/Search'))
const File      = React.lazy(() => import('./views/subjects/File'))

const routes = [
  { path: '/', exact: true, name: 'Home' },
  { path: '/subjects', name: 'Subjects', element: Subjects },
  { path: '/search', name: 'Search', element: Search },
  { path: '/subjects/:id', name: 'Subject', element: Subject },
  { path: '/subjects/:subjectId/:moduleId/:filename', name: 'File', element: File },
]

export default routes
