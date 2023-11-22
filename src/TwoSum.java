import java.util.Arrays;

public class TwoSum {

    public static void main(String[] args) {
        int[] nums = {3,3};
        System.out.println(Arrays.toString(twoSum(nums, 6)));
    }

    public static int[] twoSum(int[] nums, int target) {
        int X = 0;
        int Y = 0;
        for(int i = 0; i < nums.length; i++) {
            for(int j = 0; j < nums.length; j++) {

                if  (i != j) {
                    if (nums[i] + nums[j] == target) {
                        X = j;
                        Y = i;
                    }
                }
            }
        }
        return new int[]{X, Y};
    }
}