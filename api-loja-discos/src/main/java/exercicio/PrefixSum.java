package exercicio;

public class PrefixSum {


    static void main() {
        int[] nums = {2,4,1,3,5};

        //building the prefix array
        int[] prefix = new int[nums.length + 1];

        //prefix: {0, 0, 0, 0, 0, 0}

        for (int i = 0; i < nums.length; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
            // 1st iteration:  prefix[1] = prefix[0] + nums[0] = //prefix[1] = 2
            // prefix now: {0, 2, 0, 0, 0, 0}

            // 2nd iteration: prefix[2] = prefix[1] + nums[1] -> prefix[2] =  6
            // prefix now: {0, 2, 6, 0, 0, 0}

            // 3rd iteration: prefix[3] = prefix[2] + nums[2] -> prefix[3] = 7
            // prefix now: {0, 2, 6, 7, 0, 0}

            // 4th iteration: prefix[4] = prefix[3] + nums[3] -> prefix[4] = 10
            // prefix now: {0, 2, 6, 7, 10, 0}

            //5th iteration: prefix[5] = prefix[4] + nums[4] -> prefix[5] = 15
            //prefix now: {0,2,6,7,10,15}

            // agora sem o i + 1

            //prefix[i] = prefix[i] + nums[i];
            // 1st iteration: prefix[0] = prefix[0] + nums[0] -> prefix[0] = 2
            //prefix: [2, 0, 0, 0, 0 ,0]
            //2nd iteration: prefix[1] = prefix[1] + nums[1] -> prefix[1] = 4
            // array vai ser a mesma coisa do original

            // agora sem o nums.length *+ 1*
            // prefix[0, 0, 0, 0, 0]
            //1st iteration: prefix[1] = prefix[0] + nums[0] -> prefix[1] = 2
            // prefix: [0, 2, 0, 0, 0]
            //2nd iteration: prefix[2] = prefix[1] + nums[1] ->  prefix[1] = 6
            // prefix: [0, 2, 6, 0, 0]
            // 3rd iteration: prefix[3] = prefix[2] + nums[2] -> 7
            // prefix: [0, 2, 6, 7, 10]
        }

    }

}
