import React, {useCallback} from 'react'
import {useDropzone} from 'react-dropzone'
import axios from 'axios'

const MyDropzone = ( {uuid} ) => 
{
    const onDrop = useCallback(acceptedFiles => 
    {
        const file = acceptedFiles[0]
        const formData = new FormData()
        formData.append("file", file)
        axios.post(
            `http://localhost:8080/api/v1/${uuid}/file/upload`,
            formData,
            {
                headers:{"Content-Type":"multipart/form-data"}
            }
        ).then(() =>
        {
            console.log("file successfully uploaded")
        }).catch(error =>
        {
            console.log(error)   
        })
    }, [])

    const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop })

    return (
        <div {...getRootProps()}>
            <input {...getInputProps()} />
            {
                isDragActive ?
                    <p>Drop the files here ...</p> :
                    <p>Drag 'n' drop some files here, or click to select files</p>
            }
        </div>
    )
}

export default MyDropzone;
