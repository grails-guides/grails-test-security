package grails.test.security

import grails.rest.RestfulController

class ApiAnnouncementController extends RestfulController {
    static responseFormats = ['json']

    ApiAnnouncementController() {
        super(Announcement)
    }
}