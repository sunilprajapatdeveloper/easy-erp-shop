<template>
  <div class="terms-wrap pb-100">
    <div class="single-terms">
      <h3>Legal Disclaimer:</h3>
      <p>
        We may collect personal identification information from Users in a
        variety of ways, including, but not limited to, when Users visit our
        site, subscribe to the newsletter, fill out a form, and in connection
        with other <strong>activities</strong>, services, features or resources
        we make available on our Site. Users may be asked for, as appropriate,
        name, email address, mailing address, phone number,
        <router-link to="/">company name</router-link>. We will collect personal
        identification information from Users only if they voluntarily consent
        such information to us. Users can always refuse to supply personally
        identification information, except that it may prevent them from
        engaging in certain Site related activities.
      </p>
    </div>
    <div class="single-terms">
      <h3>Credit Reporting Terms of Service</h3>
      <p>
        Lorem ipsum dolor sit, amet consectetur adipisicing elit. Harum, quod.
        Ratione ex delectus quis tenetur odio non alias numquam official ipsum
        dolor sit, amet consectetur adipisicing elit. Accusamus, laborum.
      </p>
    </div>
    <div class="single-terms">
      <h3>Ownership of Site Agreement to Terms of Use</h3>
      <p>
        Lorem, ipsum dolor sit amet consectetur adipisicing elit. Maxime nulla
        minus quasi. Voluptatem, facilis saepe ullam autem magni quod sint
        tempore, eius molestias aliquam debitis. Neque saepe dignissimos
        repudiandae fuga.
      </p>
      <p>
        Lorem ipsum dolor sit amet consectetur, adipisicing elit. Nihil eveniet
        quas dignissimos doloribus ea pariatur corrupti rerum deserunt, ipsum,
        ipsa eos veniam aspernatur fuga, optio soluta? Libero neque reiciendis
        cupiditate dolores nam. Earum eius similique sapiente. Iure, sit non. At
        fuga ipsam veniam.
      </p>
    </div>
    <div class="single-terms">
      <h3>Provision of Services</h3>
      <p>
        Lorem ipsum dolor sit amet consectetur, adipisicing elit. Nihil eveniet
        quas dignissimos doloribus ea pariatur corrupti rerum deserunt, ipsum,
        ipsa eos veniam aspernatur fuga, optio soluta? Libero neque reiciendis
        cupiditate dolores nam. Earum eius similique sapiente. Iure, sit non. At
        fuga ipsam veniam.
      </p>
    </div>
    <div class="single-terms">
      <h3>Limitation of Liability</h3>
      <p>
        Lorem, ipsum dolor sit amet consectetur adipisicing elit. Maxime nulla
        minus quasi. Voluptatem, facilis saepe ullam autem magni quod sint
        tempore, eius molestias aliquam debitis. Neque saepe dignissimos
        repudiandae fuga.
      </p>
    </div>
    <div class="single-terms">
      <h3>Accounts, Passwords and Security</h3>
      <p>
        Lorem, ipsum dolor sit amet consectetur adipisicing elit. Maxime nulla
        minus quasi. Voluptatem, facilis saepe ullam autem magni quod sint
        tempore, eius molestias aliquam debitis. Neque saepe dignissimos
        repudiandae fuga.
      </p>
      <p>
        Lorem ipsum dolor sit amet consectetur, adipisicing elit. Nihil eveniet
        quas dignissimos doloribus ea pariatur corrupti ullam autem magni quod
        sint tempore saepe ullam autem magni.
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: "TermsConditions",
};
</script>

<style lang="scss" scoped>
.terms-wrap {
  h1,
  h2,
  h3,
  h4,
  h5,
  h6 {
    margin-bottom: 20px;
  }
  p {
    margin-bottom: 15px;
    color: var(--optioanlColor);
    &:last-child {
      margin-bottom: 0;
    }
  }
  .single-terms {
    margin-bottom: 40px;
    &:last-child {
      margin-bottom: 0;
    }
  }
}
</style>