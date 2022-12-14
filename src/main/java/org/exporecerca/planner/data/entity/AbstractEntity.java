package org.exporecerca.planner.data.entity;

import java.util.UUID;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.Type;
import org.optaplanner.core.api.domain.lookup.PlanningId;

@MappedSuperclass
public abstract class AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		}
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractEntity)) {
			return false; // null or other class
		}
		AbstractEntity other = (AbstractEntity) obj;

		if (id != null) {
			return id.equals(other.id);
		}
		return super.equals(other);
	}
}
