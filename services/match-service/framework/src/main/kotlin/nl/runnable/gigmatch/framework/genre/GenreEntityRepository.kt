package nl.runnable.gigmatch.framework.genre

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GenreEntityRepository : JpaRepository<SkillEntity, UUID>
