package example.grails

import grails.gorm.services.Service

@Service(Announcement)
interface AnnouncementService {
    Announcement save(String message)
}
