package grails.test.security

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.apache.commons.lang.builder.HashCodeBuilder

@ToString(cache=true, includeNames=true, includePackage=false)
class UserSecurityRole implements Serializable {

	private static final long serialVersionUID = 1

	User user
	SecurityRole securityRole

	UserSecurityRole(User u, SecurityRole r) {
		this()
		user = u
		securityRole = r
	}

	@Override
	boolean equals(other) {
		if (!(other instanceof UserSecurityRole)) {
			return false
		}

		other.user?.id == user?.id && other.securityRole?.id == securityRole?.id
	}

	@Override
	int hashCode() {
		def builder = new HashCodeBuilder()
		if (user) builder.append(user.id)
		if (securityRole) builder.append(securityRole.id)
		builder.toHashCode()
	}

	static UserSecurityRole get(long userId, long securityRoleId) {
		criteriaFor(userId, securityRoleId).get()
	}

	static boolean exists(long userId, long securityRoleId) {
		criteriaFor(userId, securityRoleId).count()
	}

	private static DetachedCriteria criteriaFor(long userId, long securityRoleId) {
		UserSecurityRole.where {
			user == User.load(userId) &&
			securityRole == SecurityRole.load(securityRoleId)
		}
	}

	static UserSecurityRole create(User user, SecurityRole securityRole, boolean flush = false) {
		def instance = new UserSecurityRole(user: user, securityRole: securityRole)
		instance.save(flush: flush, insert: true)
		instance
	}

	static boolean remove(User u, SecurityRole r, boolean flush = false) {
		if (u == null || r == null) return false

		int rowCount = UserSecurityRole.where { user == u && securityRole == r }.deleteAll()

		if (flush) { UserSecurityRole.withSession { it.flush() } }

		rowCount
	}

	static void removeAll(User u, boolean flush = false) {
		if (u == null) return

		UserSecurityRole.where { user == u }.deleteAll()

		if (flush) { UserSecurityRole.withSession { it.flush() } }
	}

	static void removeAll(SecurityRole r, boolean flush = false) {
		if (r == null) return

		UserSecurityRole.where { securityRole == r }.deleteAll()

		if (flush) { UserSecurityRole.withSession { it.flush() } }
	}

	static constraints = {
		securityRole validator: { SecurityRole r, UserSecurityRole ur ->
			if (ur.user == null || ur.user.id == null) return
			boolean existing = false
			UserSecurityRole.withNewSession {
				existing = UserSecurityRole.exists(ur.user.id, r.id)
			}
			if (existing) {
				return 'userRole.exists'
			}
		}
	}

	static mapping = {
		id composite: ['user', 'securityRole']
		version false
	}
}
