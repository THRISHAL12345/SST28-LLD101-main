public class AttendanceScanner implements AttendanceScanning, PowerControl {

    @Override
    public void powerOn() { }

    @Override
    public void powerOff() { }

    @Override
    public int scanAttendance() {
        return 3;
    }
}