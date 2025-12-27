<template>
  <div class="row justify-content-center pb-75">
    <div class="col-xl-4 col-md-6">
      <div class="card contact-card border-0 shadow-none">
        <h6 class="fs-20 text-title lh-1">New York</h6>
        <ul class="list-style">
          <li>
            <img src="../../../assets/img/icons/mail.svg" alt="Image" />
            <a href="mailto:hello@EasyERPShop.com"> hello@EasyERPShop.com </a>
          </li>
          <li>
            <img src="../../../assets/img/icons/map.svg" alt="Image" />#867-859
            Sit Rd. Azusa, New York 39531
          </li>
          <li>
            <img src="../../../assets/img/icons/phone.svg" alt="Image" />
            <a href="tel:12345678910"> +1 234 567 8910 </a>
          </li>
        </ul>
      </div>
    </div>
    <div class="col-xl-4 col-md-6">
      <div class="card contact-card border-0 shadow-none">
        <h6 class="fs-20 text-title">Canada</h6>
        <ul class="list-style">
          <li>
            <img src="../../../assets/img/icons/mail.svg" alt="Image" />
            <a href="mailto:hello@EasyERPShop.com"> hello@EasyERPShop.com </a>
          </li>
          <li>
            <img src="../../../assets/img/icons/map.svg" alt="Image" />127-859
            Mailibo street, Cansus, Canada
          </li>
          <li>
            <img src="../../../assets/img/icons/phone.svg" alt="Image" />
            <a href="tel:12345678910"> +1 234 567 1234 </a>
          </li>
        </ul>
      </div>
    </div>
    <div class="col-xl-4 col-md-6">
      <div class="card contact-card border-0 shadow-none">
        <h6 class="fs-20 text-title">Switzerland</h6>
        <ul class="list-style">
          <li>
            <img src="../../../assets/img/icons/mail.svg" alt="Image" />
            <a href="mailto:hello@EasyERPShop.com"> hello@EasyERPShop.com </a>
          </li>
          <li>
            <img src="../../../assets/img/icons/map.svg" alt="Image" />3217-555
            Sir Josef Aveneu, Switzerland
          </li>
          <li>
            <img src="../../../assets/img/icons/phone.svg" alt="Image" />
            <a href="tel:12345678910"> +22 234 8910 </a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ContactInfo",
};
</script>

<style lang="scss" scoped>
.contact-card {
  padding: 35px 30px;
  margin-bottom: 25px;
  h6 {
    letter-spacing: -0.4px;
    margin-bottom: 18px;
  }
  ul {
    li {
      margin-bottom: 6px;
      color: var(--optionalColor);
      position: relative;
      padding-left: 30px;
      img {
        position: absolute;
        top: 6px;
        left: 0;
      }
      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}
</style>