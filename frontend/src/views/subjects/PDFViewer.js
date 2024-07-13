import React, { useState, useEffect } from 'react';
import { PDFDocument } from 'pdf-lib';
import HintField from './HintField';

const PDFSplitter = props => {
    const [pages, setPages] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const splitFirstPDF = async () => {
            setLoading(true);
            const existingPdfBytes = await fetch(props.pdfUrl).then((res) => res.arrayBuffer());
            const pdfDoc = await PDFDocument.load(existingPdfBytes);
            const totalPages = pdfDoc.getPageCount();
            const pagesArray = [];
            const firstPage = props.currentPage? Number(props.currentPage)-1 : 0;
            
            for (let i = 0; i < totalPages; i++) {
                pagesArray.push('');
            }
            setPages(pagesArray);
            
            // First page for performance
            const newPdf = await PDFDocument.create();
            const [copiedPage] = await newPdf.copyPages(pdfDoc, [firstPage]);
            newPdf.addPage(copiedPage);
            const pdfBytes = await newPdf.save();
            const pdfBlob = new Blob([pdfBytes], { type: 'application/pdf' });
            const pdfUrl = URL.createObjectURL(pdfBlob);
            setPages(prev=>prev.map((item, index) => index == firstPage ? pdfUrl : item));
            
            setLoading(false);
        };

        const splitRestPDF = async () => {
            const existingPdfBytes = await fetch(props.pdfUrl).then((res) => res.arrayBuffer());
            const pdfDoc = await PDFDocument.load(existingPdfBytes);
            const totalPages = pdfDoc.getPageCount();
            const pagesArray = [];
            const firstPage = props.currentPage? Number(props.currentPage)-1 : 0;
            
            for (let i = 0; i < totalPages; i++) {
                if (i !== firstPage){
                    const newPdf = await PDFDocument.create();
                    const [copiedPage] = await newPdf.copyPages(pdfDoc, [i]);
                    newPdf.addPage(copiedPage);
                    const pdfBytes = await newPdf.save();
                    const pdfBlob = new Blob([pdfBytes], { type: 'application/pdf' });
                    const pdfUrl = URL.createObjectURL(pdfBlob);
                    setPages(prev=>prev.map((item, index) => index == i ? pdfUrl : item));
                }
            }
        };

        splitFirstPDF().then(()=>splitRestPDF());
        
    }, [props.pdfUrl]);

    const incrementPage = () => {
        if(!props.inputActive){
            props.setCurrentPage((prevPage) => (prevPage < pages.length ? prevPage + 1 : prevPage));
        }
    }

    const decrementPage = () => {
        if(!props.inputActive){
            props.setCurrentPage((prevPage) => (prevPage > 1 ? prevPage - 1 : prevPage));
        }
    }
    
    useEffect(() =>{
        const keyDownHandler = e => {
            if (e.key === 'ArrowRight'){
                incrementPage();
            }
            if (e.key === 'ArrowLeft')
                decrementPage();
        }
        
        if (pages){
            document.addEventListener("keydown", keyDownHandler);
            return () => {
                document.removeEventListener("keydown", keyDownHandler);
            };
        }
    },[pages]);

    const updatePage = event => {
        props.setCurrentPage(Number(event.target.value))
    }

    const handleAdd = event => {
        props.setAdd(prev => !prev)
    }

    return (
        <div>
            {loading ? (<p>Loading...</p>) : (
                <>
                    <HintField
                        currentPage={props.currentPage}
                        filename={props.filename}
                        moduleId={props.moduleId}
                        subjectId={props.subjectId}
                        token={props.token}
                        add={props.add}
                        setAdd={props.setAdd}
                    />
                    <div>
                        <button onClick={decrementPage} disabled={props.currentPage <= 1}>
                            Previous
                        </button>
                        <button onClick={incrementPage} disabled={props.currentPage === pages.length}>
                            Next
                        </button>
                        <button onClick={(handleAdd)} name="add" style={{float:"right"}}>Add</button>
                        <p>
                            Page <input 
                                    type='text' 
                                    value={props.currentPage} 
                                    size='5'
                                    onChange={updatePage}
                                 /> of {pages.length}
                        </p>
                    </div>
                    <div style={{ overflow: 'hidden' }}>
                        {pages.map((pageUrl, index) => (
                            <iframe
                                key={index+1}
                                src={`${pageUrl}#toolbar=0`}
                                width="100%"
                                height="500px"
                                style={{
                                    border: 'none',
                                    marginBottom: '10px',
                                    display: index+1 === props.currentPage ? 'block' : 'none',
                                }}
                                title={`Page ${index + 1}`}
                            />
                        ))}
                    </div>
                </>
            )}
        </div>
    );
};

export default PDFSplitter;
