package tests;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.*;
import ist.meic.pa.FunctionalProfilerExtended.LimitScope;

public class TestA {
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: Tests test1 ... testn");
			System.exit(1);
		}
		
		for (String test : args) {
			try {
				Method m = TestA.class.getDeclaredMethod(test);
				m.invoke(null);	
			} catch(InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
				// Should not happen
				System.out.println("Test method does not exist: " + test);
			}
		}
	}

	static void test1() {
		new PersonA();
	}

	static void test2() {
		new PersonA().selfIntroduce();
	}

	static void test3() {
		new PersonA().switchName("Foo");
	}

	static void test4() {
		new PersonA().firstname += "Bar";
	}

	static void test5() {
		PersonA p = new PersonA();
		for(int i = 0; i < 5; i++) {
			p.celebrateBirthday();
		}

		while (p.age < 70) {
			p.celebrateBirthday();
		}
	}

	static void test6(){
		new PersonA("Foo", "Bar");
	}

	static void test7(){
		String s = new PersonA("Foo", "Bar").firstname + new PersonA("Foo", "Bar").surname + "new PersonA(\"Foo\", \"Bar\").surname";
	}

	static void test8() {
		new PersonA(new PersonA());
	}

	static void test9() {
		new PersonA(new StudentA("Harry", "Potter"));
	}

	static void test10() {
		new StudentA();
	}
 
	static void test11() {
		StudentA t = new StudentA();
		new ProfessorA().grade(t);
		new ProfessorA().grade(t);

	}

	static void test12() {
		ProfessorA professor = new ProfessorA();
		StudentA student = new StudentA();
		PersonA person = new PersonA();
		professor.firstname = "Prof";
		student.firstname = "Student";
		person.firstname = "Person";
	}

	static void test13() {
		ProfessorA p = new ProfessorA();
		p.grade(new StudentPAvaA()); 
	}

	static void test14() {
		tests.sampleC.Example.test();
		tests.sampleA.Example.test();
		tests.sample0.Example.test();
	}

	static void test15() {
		CarA car = new CarA(10);
	}

	static void test16() {
		CarA car = new CarA();
	}

	static void test17() {
		try(MyFakeFileWriterA in = new MyFakeFileWriterA("WingardiumLeviosa_ForBeginners.pdf")) {
			in.readLine();
		} catch(IOException e) { 
			// Do you really think a fake file writer is so powerful that would throw an exception ? O:
		}
	}

	static void test18() {
		for (int i = 1; i < 18; i++) {
			try {
				Method m = TestA.class.getDeclaredMethod("test" + i);
				m.invoke(null);	
			} catch(InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
				// Should not happen
				System.out.println("Test method does not exist: test" + i);
			}
		}
	}
}

// Test Classes
class PersonA {
	String firstname;
	String surname;

	int age;

	PersonA() {}

    @LimitScope
	PersonA(String firstname, String lastname) {
		this.firstname = firstname;
		this.surname += lastname;
	}
    
	PersonA(PersonA p) {
		this.firstname = p.firstname;
		p.surname = "";
		this.surname = p.surname;
	}
    
	PersonA(StudentA t) {
		this.firstname = t.firstname;
		t.firstname = "Ron";
		this.surname = t.surname;
		t.age = this.age + 20;
	}
	String selfIntroduce() {
		return this.firstname + " " + this.surname;
	}

    @LimitScope
	void switchName(String name) { 
		this.firstname = name;
	}

	void celebrateBirthday() {
		this.age += 1;
	}
}

class ProfessorA extends PersonA {
	
	void grade(StudentA t) {
		t.mark = 20;
		t.addFavorite(this);
	}

	void grade(StudentPAvaA tp) {
		tp.mark = 20;
		tp.addFavorite(this);
		tp.pass = true;
	}
}

class StudentA extends PersonA {
	int mark;
	ProfessorA[] favoriteProfessors;
	StudentA() {
		this.favoriteProfessors = new ProfessorA[5];
	}


	StudentA(String firstname, String lastname) {
		this.firstname = firstname; this.surname = lastname;
	}

	StudentA(StudentA other) {
		StudentA otherThis = this;
		otherThis.firstname = "Foo";
		other.firstname = "Foo";
		otherThis.surname = "Bar";
		other.age = 0;
		other.favoriteProfessors = new ProfessorA[5];
	}
	void addFavorite(ProfessorA p) {
		if (favoriteProfessors != null) {
			int i = 0;
			for(; i < 5 && favoriteProfessors[i] != null; i++) ;
			if (i < 5) favoriteProfessors[i] = p;
		}
	}
}

class StudentPAvaA extends StudentA {
	boolean pass = false;
}


// Test Classes
class CarA{
	int maxSpeed;
	float fuelCapacity;

	static class Motor {
		boolean on;
		int maxSpeed;
		
		Motor(CarA c) {
		    on = false;
		    maxSpeed = c.maxSpeed;
		    c.maxSpeed = 200;
		}
		
		private void turnOn() {
		    on = true;
		    maxSpeed *= 2;
		}
    }

	CarA(float fuelCapacity) {
		this.maxSpeed = 15;
		this.fuelCapacity = this.fuelCapacity + fuelCapacity;
		this.raiseMaxSpeed(200); // I wanna move faster than a snail, for god's sake...
	}

	CarA() {
		this.maxSpeed = 20; 
		this.fuelCapacity = 6;

		new Motor(this).turnOn();
	}

	void raiseMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
}

class MyFakeFileWriterA implements AutoCloseable {
	String in;

	MyFakeFileWriterA(String filename) { 
		this.in += filename; 
		String s = "Opening file this.filename"; // Oops! What now?
	}

	String readLine() {
		return "The first and foremost important aspect of Wingardium Leviosa is to learn the proper accent! Try with me: *wingaaardiuum* *lééviosa*.";
	}

	@Override
	public void close() throws IOException {
		this.in = "";
	}	
}
