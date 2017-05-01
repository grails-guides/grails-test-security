package grails.test.security

import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK
import grails.transaction.Transactional

@SuppressWarnings('LineLength')
@Transactional(readOnly = true)
class AnnouncementController {

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Announcement.list(params), model:[announcementCount: Announcement.count()]
    }

    def show(Announcement announcement) {
        respond announcement
    }

    @SuppressWarnings(['GrailsMassAssignment', 'FactoryMethodName'])
    def create() {
        respond new Announcement(params)
    }

    @Transactional
    def save(Announcement announcement) {
        if (announcement == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (announcement.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond announcement.errors, view:'create'
            return
        }

        announcement.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'announcement.label', default: 'Announcement'), announcement.id])
                redirect announcement
            }
            '*' { respond announcement, [status: CREATED] }
        }
    }

    def edit(Announcement announcement) {
        respond announcement
    }

    @Transactional
    def update(Announcement announcement) {
        if (announcement == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (announcement.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond announcement.errors, view:'edit'
            return
        }

        announcement.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'announcement.label', default: 'Announcement'), announcement.id])
                redirect announcement
            }
            '*' { respond announcement, [status: OK] }
        }
    }

    @Transactional
    def delete(Announcement announcement) {

        if (announcement == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        announcement.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'announcement.label', default: 'Announcement'), announcement.id])
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'announcement.label', default: 'Announcement'), params.id])
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
