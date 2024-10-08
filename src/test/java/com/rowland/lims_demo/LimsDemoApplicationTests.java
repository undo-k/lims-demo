package com.rowland.lims_demo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class LimsDemoApplicationTests {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private  LabOrderRepository labOrderRepository;

	@Autowired
	private PhysicianRepository physicianRepository;



	@Test
	public void createPatientThenFindPatient() throws Exception {
		Patient john = new Patient("John", "Smith", "1963-11-22", "M");
		entityManager.persistAndFlush(john);

		Patient found = patientRepository.findByFirstNameAndLastNameAndDateOfBirth("John", "Smith", LocalDate.parse("1963-11-22"));

		assertThat(found.getId()).isEqualTo(john.getId());
	}

	@Test
	public void updatePatientAddress() throws Exception {
		Patient john = new Patient("John", "Smith", "1963-11-22", "M");
		String addy = "123 High Street Abyssal Woods, FL 335522";
		john.setAddress(addy);
		entityManager.persistAndFlush(john);

		Optional<Patient> found = patientRepository.findById(john.getId());
		assertThat(found.isPresent()).isEqualTo(true);
		assertThat(found.get().getAddress()).isEqualTo(addy);
	}

	@Test
	public void createPhysicianThenFindPhysician() {
		Physician p = new Physician("Doctor", "Doctor", "Hospital", Arrays.asList("Insurance A", "Insurance B"));
		entityManager.persistAndFlush(p);

		Physician found = physicianRepository.findByFirstNameAndLastNameAndHospital("Doctor", "Doctor", "Hospital");

		assertThat(found.getId()).isEqualTo(p.getId());
		assertThat(found.getFirstName()).isEqualTo(p.getFirstName());
		assertThat(found.getLastName()).isEqualTo(p.getLastName());
	}


	@Test
	public void bottomUpCreation() throws Exception {
		Patient patient = new Patient("John", "Smith", "1963-11-22", "M");
		entityManager.persistAndFlush(patient);

		Sample sample = new Sample();
		entityManager.persistAndFlush(sample);

		LabTest test = new LabTest(LabTest.TestType.FUN, sample);
		entityManager.persistAndFlush(test);

		Physician physician = new Physician("Doctor", "Doctor", "Hospital", Arrays.asList("Insurance A", "Insurance B"));
		entityManager.persistAndFlush(physician);

		List<LabTest> labTests = List.of(test);
		LabOrder order = new LabOrder(patient, physician, labTests);
		entityManager.persistAndFlush(order);


		Patient p = patientRepository.findById(patient.getId()).orElseThrow();
		assertThat(Objects.equals(p.getLastName(), patient.getLastName())).isEqualTo(true);

	}




}
