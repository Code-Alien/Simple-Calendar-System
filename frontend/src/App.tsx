import './App.css'
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom'
import {AddEventPage, CalendarPage, EventDetailsPage} from './pages'

function App() {
  return (
    <Router>
      <div className="app">
        <Routes>
          <Route path="/" element={<Navigate to="/calendar" replace/>}/>
          <Route path="/calendar" element={<CalendarPage/>}/>
          <Route path="/add-event" element={<AddEventPage/>}/>
          <Route path="/event/:id" element={<EventDetailsPage/>}/>
        </Routes>
      </div>
    </Router>
  )
}

export default App
