import React, { useState, useEffect } from 'react'
import MyDropzone from './components/MyDropzone'
import axios from 'axios'

const UserProfiles = () => 
{
    const [userProfiles, setUserProfiles] = useState([])

    const fetchUserProfiles = () => 
    {
        axios.get('http://localhost:8080/api/v1/user-profile').then(res => 
        {
            console.log(res);
            setUserProfiles(res.data)
        })
    }

    useEffect(() => 
    {
        fetchUserProfiles();
    }, [])

    return userProfiles.map((userProfile, index) => 
    {
        return (
            <div key={index}>
                {/* TODO: preview */}
                <br/>
                <br/>
                <MyDropzone {...userProfile}/>
                <h1>{userProfile.username}</h1>
                <p>{userProfile.uuid}</p>
                <br/>
            </div>)
    })
}

const App = () => 
{
    return (
        <div className='App'>
            <UserProfiles />
        </div>
    )
}

export default App
