import './App.css'
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom'
import {AddEventPage, CalendarPage, EventDetailsPage} from './pages'
import {createTheme, ThemeProvider} from '@mui/material/styles'
import CssBaseline from '@mui/material/CssBaseline'


//todo: remove theme, extract styles
const theme = createTheme({
  palette: {
    mode: 'light',
  },
})

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline/>
      <Router>
        <div style={{width: '100%', minHeight: '100vh'}}>
          <Routes>
            <Route path="/" element={<Navigate to="/calendar" replace/>}/>
            <Route path="/calendar" element={<CalendarPage/>}/>
            <Route path="/add-event" element={<AddEventPage/>}/>
            <Route path="/event/:id" element={<EventDetailsPage/>}/>
          </Routes>
        </div>
      </Router>
    </ThemeProvider>
  )
}

export default App
