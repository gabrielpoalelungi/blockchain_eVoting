import './App.css';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Provider } from 'react-redux';
import { store } from './pages/store.js';
import { Main } from './pages/Main';
import SignIn from './pages/SignIn';
import BasePage from './pages/BasePage';
import SignUp from './pages/SignUp';


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

  return (
    <div className="App">
        <QueryClientProvider client={client}>
        <Provider store={store}>
          <BrowserRouter>
            {/* <Navbar /> */}
            <Routes>
              <Route path="/" element={<BasePage />}/>
              <Route path="/signin" element={<SignIn />}/>
              <Route path="/signup" element={<SignUp />}/>
            </Routes>
          </BrowserRouter>
        </Provider>
      </QueryClientProvider>
    </div>
  );
}

export default App;
