package nl.runnable.gigmatch.framework.vacancy

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SkillEntityRepository : JpaRepository<SkillEntity, UUID>
