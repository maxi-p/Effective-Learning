import React from 'react'

// Notes
const Dictionary = React.lazy(() => import('./views/notes/dictionary/Dictionary'))
const Categories = React.lazy(() => import('./views/notes/categories/Categories'))
const AddNote    = React.lazy(() => import('./views/notes/add-note/AddNote'))

// Train
const Train     = React.lazy(() => import('./views/training/tests/Tests'))
const Tests     = React.lazy(() => import('./views/training/train/Train'))
const Dashboard = React.lazy(() => import('./views/training/dashboard/Dashboard'))
const Generate  = React.lazy(() => import('./views/training/generate/Generate'))

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
  { path: '/notes/dictionary', name: 'Notes', element: Dictionary },
  { path: '/notes/categories', name: 'Categories', element: Categories },
  { path: '/notes/add', name: 'AddNote', element: AddNote },
  { path: '/training/train', name: 'Train', element: Train },
  { path: '/training/tests', name: 'Tests', element: Tests },
  { path: '/training/dashboard', name: 'Dashboard', element: Dashboard },
  { path: '/training/generate', name: 'Generate', element: Generate },
]

export default routes
