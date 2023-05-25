import './App.css';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Provider } from 'react-redux';
import { store } from './pages/store.js';
import SignIn from './pages/SignIn';
import BasePage from './pages/BasePage';
import SignUp from './pages/SignUp';
import WhatAreWeVotingForPage from './pages/WhatAreWeVotingForPage';
import HowToVotePage from './pages/HowToVotePage';
import * as React from 'react';
import WhoAreTheCandidatesPage from './pages/WhoAreTheCandidatesPage';
import CastAVotePage from './pages/CastAVotePage';
import ResultsPage from './pages/ResultsPage';
import AdminBasePage from './pages/AdminBasePage'
import AdminAddCandidatePage from './pages/AdminAddCandidatePage'
import AdminEditCandidatePage from './pages/AdminEditCandidatePage';
import AdminCreateVoteSessionPage from './pages/AdminCreateVoteSessionPage';
import AdminEditVoteSessionPage from './pages/AdminEditVoteSessionPage';
import AdminStartEndVoteSessionPage from './pages/AdminStartEndVoteSessionPage';
import AdminEditCandidateFormPage from './pages/AdminEditCandidateFormPage';


function App() {
  const client = new QueryClient({defaultOptions :{
    queries : {
      refetchOnWindowFocus: false
    }
  }});

  // Attach an event listener to the window.onbeforeunload event
  window.onbeforeunload = function() {
    // Clear the variable from local storage
    localStorage.removeItem("jwt_token");
  };

  const[itemsActive, setItemsActive] = React.useState([false, false, false, false, false]);

  return (
    <div className="App">
        <QueryClientProvider client={client}>
        <Provider store={store}>
          <BrowserRouter>
            <Routes>
              <Route path="/" element={<BasePage itemsActive={itemsActive} setItemsActive={setItemsActive}/>}/>
              <Route path="/signin" element={<SignIn />}/>
              <Route path="/signup" element={<SignUp />}/>
              <Route path="/whatarewevotingfor" element={<WhatAreWeVotingForPage itemsActive={itemsActive} setItemsActive={setItemsActive}/>}/>
              <Route path="/howtovote" element={<HowToVotePage itemsActive={itemsActive} setItemsActive={setItemsActive}/>}/>
              <Route path="/whoarethecandidates" element={<WhoAreTheCandidatesPage itemsActive={itemsActive} setItemsActive={setItemsActive}/>}/>
              <Route path="/vote" element={<CastAVotePage itemsActive={itemsActive} setItemsActive={setItemsActive}/>}/>
              <Route path="/results" element={<ResultsPage itemsActive={itemsActive} setItemsActive={setItemsActive}/>}/>
              <Route path="/admin" element={<AdminBasePage itemsActive={itemsActive} setItemsActive={setItemsActive}/>}/>
              <Route path="/admin/addcandidate" element={<AdminAddCandidatePage itemsActive={itemsActive} setItemsActive={setItemsActive}/>}/>
              <Route path="/admin/editcandidates" element={<AdminEditCandidatePage itemsActive={itemsActive} setItemsActive={setItemsActive}/>}/>
              <Route path="/admin/createvotesession" element={<AdminCreateVoteSessionPage itemsActive={itemsActive} setItemsActive={setItemsActive}/>}/>
              <Route path="/admin/editvotesession" element={<AdminEditVoteSessionPage itemsActive={itemsActive} setItemsActive={setItemsActive}/>}/>
              <Route path="/admin/startendvotesession" element={<AdminStartEndVoteSessionPage itemsActive={itemsActive} setItemsActive={setItemsActive}/>}/>
              <Route path="/admin/editcandidateform/:candidateId" element={<AdminEditCandidateFormPage itemsActive={itemsActive} setItemsActive={setItemsActive}/>}/>
            </Routes>
          </BrowserRouter>
        </Provider>
      </QueryClientProvider>
    </div>
  );
}

export default App;
