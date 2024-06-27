import React, { useEffect, useState } from 'react'
import { useSearchParams, useNavigate } from 'react-router-dom'
import {
  CRow,
  CCol,
  CCard,
  CCardHeader,
  CCardBody,
  CCardTitle,
  CButton,
  CNav,
  CNavItem,
  CNavLink
} from '@coreui/react'


const Train = props => {
  const [searchParams] = useSearchParams();
  const [test, setTest] = useState(null);
  const [encodedResult, setEncodedResult] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    const id = searchParams.get('testId');
    if (id !== null)
    {
      fetch("http://localhost:8080/api/v1/note-test/"+id, {
        method: "GET",
        headers: {'Authorization':'Bearer '+props.token}, 
      })
      .then(res => res.json())
      .then(res => {
        setTest(res)
      });
    }
  },[]);

  const [showQuestion, setShowQuestion] = useState(true);
  const [showAnswer, setShowAnswer] = useState(false);
  const [curQuestion, setCurQuestion] = useState(0);

  const nextQuestion = event => {

    setShowAnswer(false);
    setShowQuestion(true);
    if (event.target.name === "passed")
    {
      setEncodedResult(prev => {
        return prev+`Q${curQuestion+1}:C,`
      });
    }
    else
    {
      setEncodedResult(prev => {
        return prev+`Q${curQuestion+1}:W,`
      });
    }
    setCurQuestion(prev => {
      if(prev === test.numberOfQuestions){
        return prev;
      }
      else{
        return ++prev;
      }
    })
  };

  useEffect(() => {
    if (test && curQuestion === test.numberOfQuestions)
    {
      console.log("testname: "+searchParams.get('testName'))
      const data = 
      {
        encodedResult: encodedResult,
        testId: searchParams.get('testId'),
        testName: searchParams.get('testName')
      }
      fetch("http://localhost:8080/api/v1/note-test/result", {
        method: "POST",
        headers: {'Content-Type': 'application/json', 'Authorization':'Bearer '+props.token}, 
        body: JSON.stringify(data)
      })
      .then(res => res.json())
      .then(res => {
        console.log("Test results submitted! response:", res);
        navigate('/training/dashboard');
      });
    }
  },[encodedResult]);

  console.log("Encoded result: "+encodedResult);

  const handleToggleShow = () => {
    setShowAnswer(prev => !prev);
    setShowQuestion(prev => !prev);
  };

  return (
      <CRow>
        <CCol xs={12}>
        {test && <CCard className="text-center">
          <CCardHeader>
            <CNav variant="pills" className="card-header-pills">
              <CNavItem>
                <CNavLink
                  name="passed" 
                  onClick={nextQuestion}
                >Passed
                </CNavLink>
              </CNavItem>
              <CNavItem>
                <CNavLink 
                  name="failed"
                  onClick={nextQuestion}
                >Failed
                </CNavLink>
              </CNavItem>
              <CNavItem>
                <CNavLink 
                  disabled>{curQuestion}/{test.numberOfQuestions}
                </CNavLink>
              </CNavItem>
            </CNav>
          </CCardHeader>
          {showQuestion && <CCardBody>
            <CCardTitle>{test.questions[curQuestion===test.numberOfQuestions?test.numberOfQuestions-1:curQuestion].question}</CCardTitle>
            <CButton 
              color="primary" 
              type="button"
              onClick={handleToggleShow}>
              Answer
            </CButton>
          </CCardBody>}
          {showAnswer && <CCardBody>
            <CCardTitle>{test.questions[curQuestion===test.numberOfQuestions?test.numberOfQuestions-1:curQuestion].answer}</CCardTitle>
            <CButton 
              color="primary" 
              type="button"
              onClick={handleToggleShow}>
              Question
            </CButton>
          </CCardBody>}
        </CCard>}
        </CCol>
      </CRow>
        )
}

export default Train
