import React, {useState, useEffect} from 'react'
import axios from 'axios'

const UserProfiles = () => 
{
  const fetchUserProfiles = () =>
  {
    axios.get('http://localhost:8080/api/v1/user-profile').then(res =>
    {
      console.log(res);
    }
    )
  }
  
  useEffect(() =>
  {
    fetchUserProfiles();  
  },[])

  return <h1>Hello from frontend</h1>
}

const App = () => 
{
  return (
    <div className='App'>
      <UserProfiles/>
    </div>
  )
}

export default App
