package com.example.status_patient_home

import java.util.Date

class EventCreator {
    var _event = Event("", Date(), "", "")
    public var eList = mutableListOf<Event>()
fun createEvent(provider: String, date: Date, eventType: String, patient: String){
    var event =  Event(provider, date, eventType, patient)
    //Update database with events added
    _event = event
    eList.add(_event)
}
}