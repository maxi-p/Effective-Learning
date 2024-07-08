import React, { useState, useEffect } from 'react';
import { useParams, useSearchParams } from 'react-router-dom'
import {
  CCard,
  CCardBody,
  CCardHeader,
  CCarousel,
  CCarouselCaption,
  CCarouselItem,
  CCol,
  CRow,
} from '@coreui/react'

import { DocsExample } from 'src/components'

import AngularImg from 'src/assets/images/angular.jpg'
import ReactImg from 'src/assets/images/react.jpg'
import VueImg from 'src/assets/images/vue.jpg'

const UserFile = props => {
    const [page, setPage] = useState(1);
    const [ready, setReady] = useState(false);
    const [imageUrls, setImageUrls] = useState([]);
    const [searchParams] = useSearchParams();

    const numPages = searchParams.get("numPages");

    const {subjectId} = useParams();
    const {moduleId} = useParams();
    const {filename} = useParams();

    useEffect(() => {
      const nameWithoutExtension = filename.replace(".pdf", "")
      const arr = []
      for (let i=0; i<numPages; i++)
      {
        arr.push("")
      }
      setImageUrls(arr);
      for (let i=0; i<numPages; i++ )
      {
        fetch(`http://localhost:8080/api/v1/file-store/images/${subjectId}/${moduleId}/${nameWithoutExtension}_page-${i}.jpg`, {
          method: "GET",
          headers: {'Authorization':'Bearer '+props.token}, 
        })
        .then((response) => response.blob())
        .then((blob) => URL.createObjectURL(blob))
        .then((url) => {setImageUrls(prev=>prev.map((item, index) => index == i ? url : item))})
        .then(() => {if(i+1 == numPages) setReady(true)})
      }  
    }, []);

    const handleSlide = event => {
      setPage(prev => event.target.name === 'next'? prev==numPages? 1: (prev+1)%(numPages+1): prev===1? Number(numPages): (prev-1)%(numPages+1))
    }

    return (
        <div>
          {ready && <div>
          <button name="prev" onClick={handleSlide}>Prev</button>
          <button name="next" onClick={handleSlide}>Next</button>
          <span>{page}/{numPages}</span>
          <br/>
            {imageUrls.map((imageUrl, index) => (
              <img key={index} style={index===(page-1)?{display: 'block',width:'800px'}:{display: 'none'}} src={imageUrl} alt={`Slide ${index+1}`} />
            ))}
            </div>
          }
        </div>
    );
}

export default UserFile;
