package com.rowland.lims_demo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class LimsDemoApplicationTests {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private PatientRepository patientRepository;

	@Test
	public void insertPatient_thenGetPatientById() throws Exception {
		Patient john = new Patient("John", "Smith", "1963-11-22", "M");
		entityManager.persistAndFlush(john);

		Patient found = patientRepository.findByFirstNameAndLastNameAndDateOfBirth("John", "Smith", LocalDate.parse("1963-11-22"));

		assertThat(found.getId()).isEqualTo(john.getId());
	}

	@Test
	public void setPatientAddress() throws Exception {
		Patient john = new Patient("John", "Smith", "1963-11-22", "M");
		String addy = "123 High Street Abyssal Woods, FL 335522";
		john.setAddress(addy);
		entityManager.persistAndFlush(john);

		Optional<Patient> found = patientRepository.findById(john.getId());
		assertThat(found.isPresent()).isEqualTo(true);
		assertThat(found.get().getAddress()).isEqualTo(addy);
	}




}
