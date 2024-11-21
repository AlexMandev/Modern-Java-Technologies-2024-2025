public class CourseScheduler {
    public static void selectionSortByStartHour(int[][] courses) {
        for(int i = 0; i < courses.length - 1; i++) {
            int minIndex = i;
            for(int j = i + 1; j < courses.length; j++) {
                if(courses[j][0] < courses[minIndex][0]) {
                    minIndex = j;
                }
            }

            int[] temp = courses[minIndex];
            courses[minIndex] = courses[i];
            courses[i] = temp;
        }
    }

    public static int maxNonOverlappingCourses(int[][] courses) {
        if(courses == null || courses.length == 0)
            return 0;

        for(int[] course: courses) {
            if(course.length != 2)
                return -1;
        }

        selectionSortByStartHour(courses);

        int[] res = new int[courses.length];

        for (int i = 0; i < courses.length; i++) {
            res[i] = 1;
        }


        for(int i = 0; i < courses.length; i++) {
            for(int j = 0; j < i; j++) {
                if(courses[j][1] <= courses[i][0]) {
                    res[i] = Math.max(res[i], res[j] + 1);
                }
            }
        }

        int answer = 0;
        for(int a : res) {
            answer = Math.max(answer, a);
        }

        return answer;
    }
}


