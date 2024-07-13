import React from 'react'
import { useState, useEffect } from 'react'
import { AppContent, AppSidebar, AppFooter, AppHeader } from '../components/index'

const DefaultLayout = props => {
  const [subjects, setSubjects] = useState([]);
  const [isAddFileMenu, setIsAddFileMenu] = useState(false);
  const [refresh, setRefresh] = useState(true);

  useEffect(() => {
    if (refresh)
    {
      fetch("http://localhost:8080/api/v1/file-store/subjects", {
        method: "GET",
        headers: {'Authorization':`Bearer ${props.token}`}, 
      })
      .then(res => res.json())
      .then(res => {
        setSubjects(res)
        setRefresh(false)
      });
    }
  },[refresh]);

  return (
    <div>
        <AppSidebar />
        <div className="wrapper d-flex flex-column min-vh-100">
            <AppHeader 
                setIsAddFileMenu={setIsAddFileMenu}
            />
            <div className="body flex-grow-1">
                <AppContent
                    token={props.token} 
                    subjects={subjects}
                    isAddFileMenu={isAddFileMenu}
                    setIsAddFileMenu={setIsAddFileMenu}
                    refresh={refresh}
                    setRefresh={setRefresh}
                />
            </div>
            <AppFooter />
        </div>
    </div>
  )
}

export default DefaultLayout
