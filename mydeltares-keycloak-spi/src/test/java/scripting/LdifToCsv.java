package scripting;

import java.io.*;

public class LdifToCsv {

    /**
     * Convert LDAP export file to CSV file:
     * <p>
     * Expected argument
     * <p>
     * path to ldif file
     */
    public static void main(String[] args) {
        assert args.length == 1;

        File ldif = new File(args[0]);
        if (!ldif.exists()) return;

        File csvFile = new File(ldif.getAbsolutePath() + ".csv");
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile)))) {
            writeResults(bw, "username", "email"); // write header
            try (BufferedReader reader = new BufferedReader(new FileReader(ldif))) {
                String line = reader.readLine();

                String email = null;
                String username = null;
                while (line != null) {

                    if (line.startsWith("dn:")) {
                        //new user. write what we have to file and reset
                        if (username != null || email != null) {
                            try {
                                //write results;
                                writeResults(bw, username, email);
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        email = null;
                        username = null;
                    } else if (line.startsWith("cn:")) {
                        username = line.substring("cn:".length());
                    } else if (line.startsWith("mail:")) {
                        email = line.substring("mail:".length());
                    }
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeResults(BufferedWriter bw, String... args) throws IOException {

        StringBuilder format = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            format.append("%s;");
        }
        bw.write(String.format(format.toString(), (Object[]) args));
        bw.newLine();
        bw.flush();
    }
}
