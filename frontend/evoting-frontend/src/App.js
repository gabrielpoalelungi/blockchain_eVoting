import './App.css';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import { Provider } from 'react-redux';
import { store } from './components/store.js';
import { Main } from './pages/Main';
import Paperbase from './components/Paperbase';




function App() {
  const client = new QueryClient({defaultOptions :{
    queries : {
      refetchOnWindowFocus: false
    }
  }});

  return (
    <div className="App">
        <QueryClientProvider client={client}>
        <Provider store={store}>
          <BrowserRouter>
            {/* <Navbar /> */}
            <Routes>
              <Route path="/" element={<Paperbase />}/>
            </Routes>
          </BrowserRouter>
        </Provider>
      </QueryClientProvider>
    </div>
  );
}

export default App;
